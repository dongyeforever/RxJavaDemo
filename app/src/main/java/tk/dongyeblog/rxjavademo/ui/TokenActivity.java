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

/**
 * description:
 * author： dongyeforever@gmail.com
 * date: 2016-05-19 15:50
 */
public class TokenActivity extends BaseActivity {
    @Bind(R.id.tokenTv)
    TextView tokenTv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_token;
    }

    @Override
    protected void initView() {
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
    }

    @OnClick(R.id.requestBt)
    void upload() {
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        final FakeApi fakeApi = Network.getFakeApi();
        subscription = fakeApi.getFakeToken("fake_auth_code")
                .flatMap(new Func1<FakeToken, Observable<FakeThing>>() {
                    @Override
                    public Observable<FakeThing> call(FakeToken fakeToken) {
                        return fakeApi.getFakeData(fakeToken);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FakeThing>() {
                    @Override
                    public void call(FakeThing fakeThing) {
                        swipeRefreshLayout.setRefreshing(false);
                        tokenTv.setText("获取到的数据：\nID：" + fakeThing.id + "\n用户名：" + fakeThing.name);
                    }
                });
    }
}
