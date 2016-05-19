package tk.dongyeblog.rxjavademo.simple;

import android.widget.TextView;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import tk.dongyeblog.rxjavademo.R;
import tk.dongyeblog.rxjavademo.base.BaseActivity;
import tk.dongyeblog.rxjavademo.util.LogUtil;

/**
 * description:
 * author： dongyeforever@gmail.com
 * date: 2016-04-12 19:57
 */
public class MapActivity extends BaseActivity {
    @Bind(R.id.tv)
    TextView tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(1);
                        subscriber.onCompleted();
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer + "";
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.e("onCompleted...");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        LogUtil.e(s);
                        tv.setText(s);
                    }
                });
    }
}
