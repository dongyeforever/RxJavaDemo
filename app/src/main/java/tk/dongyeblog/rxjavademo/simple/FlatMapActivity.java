package tk.dongyeblog.rxjavademo.simple;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import tk.dongyeblog.rxjavademo.R;
import tk.dongyeblog.rxjavademo.base.BaseActivity;
import tk.dongyeblog.rxjavademo.util.LogUtil;

public class FlatMapActivity extends BaseActivity {
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.button)
    Button button;
    Observable<String[]> observable;
    Subscriber<String> subscriber;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("点击");
                observable
                        .flatMap(new Func1<String[], Observable<String>>() {
                            @Override
                            public Observable<String> call(String[] strings) {
                                LogUtil.e("Observable flatMap");
                                return Observable.from(strings);
                            }
                        })
                        .map(new Func1<String, String>() {
                            @Override
                            public String call(String s) {
                                return s;
                            }
                        })
                        .subscribe(subscriber);
            }
        });
    }

    @Override
    protected void initData() {
        final String s[] = {"数据1", "数据2", "数据3"};
        observable = Observable.create(new Observable.OnSubscribe<String[]>() {
            @Override
            public void call(Subscriber<? super String[]> subscriber) {
                LogUtil.e("Observable call");
                subscriber.onNext(s);
                subscriber.onCompleted();
            }
        });
        subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                LogUtil.e("结果 Completed!");
                tv.setText(tv.getText() + "\n结果 Completed!");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                LogUtil.e("结果 " + s);
                tv.setText(tv.getText() + "\n结果 " + s);
            }
        };
    }
}
