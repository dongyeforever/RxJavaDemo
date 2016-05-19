package tk.dongyeblog.rxjavademo.ui;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import tk.dongyeblog.rxjavademo.R;
import tk.dongyeblog.rxjavademo.base.BaseActivity;
import tk.dongyeblog.rxjavademo.util.LogUtil;

/**
 * description: RxJava 基本使用
 * author： dongyeforever@gmail.com
 * date: 2016-01-21 13:35
 */
public class RxSimpleDemo extends BaseActivity {
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.iv)
    ImageView iv;

    @Override
    protected int getLayoutId() {
        return R.layout.rx_simple_demo;
    }

    @Override
    public void initView() {
        simpleDemo();
        rxFrom();
//        rxInterval();
        rxJust();
        rxSetImageRes();
        rxBinding();
    }

    private void rxBinding() {
        //防抖处理
        RxView.clicks(iv)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        LogUtil.i("点击了...");
                    }
                });
    }

    private void simpleDemo() {
        /*
         * 第一种最简单使用
         */
        final Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    //
                    subscriber.onNext("首页");
                    subscriber.onCompleted();
                }
            }
        });

        observable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                LogUtil.i("Rx Observer onCompleted()");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                LogUtil.i("Rx Observer onNext()");
            }
        });

        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                LogUtil.i("Rx onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e("Rx onError()");
            }

            @Override
            public void onNext(String s) {
                tv.setText(s);
            }
        });
    }

    /**
     * Observable from 把其他数据转为Observables
     * From会将数组或Iterable的素具取出然后逐个发射
     */
    private void rxFrom() {
        Integer[] items = {0, 1, 2, 3, 4, 5, 6};
        Observable<Integer> myObservable = Observable.from(items);
        myObservable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtil.i("item: " + integer);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LogUtil.e("Error encountered: " + throwable.getMessage());
            }
        }, new Action0() {
            @Override
            public void call() {
                LogUtil.i("Sequence complete");
            }
        });
    }

    /**
     * interval 间隔 指定时间间隔发送数据
     */
    private void rxInterval() {
        Observable<Long> myObservable = Observable.interval(1000, TimeUnit.MILLISECONDS);
        myObservable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                LogUtil.i("along: " + aLong);
            }
        });
    }

    /**
     * 创建一个发射指定值的Observable
     * 最多有10个参数 返回一个按参数列表顺序发射这些数据的Observable
     */
    private void rxJust() {
        /*
         * 第二种 简单写法
         */
        Observable<Integer> simpleObservable = Observable.just(1, 2, 3);

        Action1<Integer> onNextAction = new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                String s = tvName.getText().toString() + integer + "\n";
                tvName.setText(s);
            }
        };
        simpleObservable.subscribe(onNextAction);

        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                LogUtil.i("Rx onComplete...");
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
            }
        };
        simpleObservable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }

    /**
     * Rx demo set imageView resource
     */
    private void rxSetImageRes() {
        final int drawableRes = R.mipmap.dongye;
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Drawable>() {
                    @Override
                    public void call(Drawable drawable) {
                        iv.setImageDrawable(drawable);
                    }
                });
    }
}
