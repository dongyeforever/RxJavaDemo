package tk.dongyeblog.rxjavademo.ui;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import tk.dongyeblog.rxjavademo.R;
import tk.dongyeblog.rxjavademo.base.BaseActivity;
import tk.dongyeblog.rxjavademo.bean.FakeThing;
import tk.dongyeblog.rxjavademo.bean.FakeToken;
import tk.dongyeblog.rxjavademo.network.Network;
import tk.dongyeblog.rxjavademo.network.api.FakeApi;
import tk.dongyeblog.rxjavademo.util.ToastUtil;

/**
 * description: token 高级 token过期后重新获取token
 * author： dongyeforever@gmail.com
 * date: 2016-05-19 16:26
 */
public class TokenAdvanceAty extends BaseActivity {
    @Bind(R.id.tokenTv)
    TextView tokenTv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    final FakeToken cachedFakeToken = new FakeToken(true);
    boolean tokenUpdated;

    @Override
    protected void initView() {
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_token_advanced;
    }

    @OnClick(R.id.invalidateTokenBt)
    void invalidateToken() {
        cachedFakeToken.expired = true;
        ToastUtil.showToast("已销毁");
    }

    @OnClick(R.id.requestBt)
    void upload() {
        tokenUpdated = false;
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        final FakeApi fakeApi = Network.getFakeApi();
        subscription = Observable.just(null)
                .flatMap(new Func1<Object, Observable<FakeThing>>() {
                    @Override
                    public Observable<FakeThing> call(Object o) {
                        return cachedFakeToken.token == null
                                ? Observable.<FakeThing>error(new NullPointerException("Token is null!"))
                                : fakeApi.getFakeData(cachedFakeToken);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                if (throwable instanceof IllegalArgumentException || throwable instanceof NullPointerException) {
                                    return fakeApi.getFakeToken("fake_auth_code")
                                            .doOnNext(new Action1<FakeToken>() {
                                                @Override
                                                public void call(FakeToken fakeToken) {
                                                    tokenUpdated = true;
                                                    cachedFakeToken.token = fakeToken.token;
                                                    cachedFakeToken.expired = fakeToken.expired;
                                                }
                                            });
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FakeThing>() {
                    @Override
                    public void call(FakeThing fakeThing) {
                        swipeRefreshLayout.setRefreshing(false);
                        String token = cachedFakeToken.token;
                        if (tokenUpdated) {
                            token += "(已更新)";
                        }
                        tokenTv.setText("token:" + token + "\n获取到的数据：\nID：" + fakeThing.id + "\n用户名：" + fakeThing.name);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        swipeRefreshLayout.setRefreshing(false);
                        ToastUtil.showToast("数据加载失败");
                    }
                });
    }

}
