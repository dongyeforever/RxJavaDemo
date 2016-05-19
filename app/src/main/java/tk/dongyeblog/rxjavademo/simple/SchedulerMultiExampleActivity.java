package tk.dongyeblog.rxjavademo.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.Executor;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import tk.dongyeblog.rxjavademo.R;

/**
 * Scheduler demo
 *
 * on 2016/1/10.
 */
public class SchedulerMultiExampleActivity extends AppCompatActivity {

    private static final String TAG = SchedulerMultiExampleActivity.class.getSimpleName();
    private TextView txt_thread_log;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx_simple_demo);
        txt_thread_log = (TextView) findViewById(R.id.tv);
        schedulerMultiExample();
    }

    private void schedulerMultiExample() {
        txt_thread_log.setText("main thread id =" + Thread.currentThread().getId());
        Log.i(TAG, "main thread id =" + Thread.currentThread().getId());
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        //
                        txt_thread_log.setText(txt_thread_log.getText() + "\n" + "Schedulers.newThread() thread id =" + Thread.currentThread().getId());
                        Log.i(TAG, "Schedulers.newThread() thread id =" + Thread.currentThread().getId());
                        return "5" + integer;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        //
                        txt_thread_log.setText(txt_thread_log.getText() + "\n" + "Schedulers.io() thread id =" + Thread.currentThread().getId());
                        Log.i("Tiny", "Schedulers.io() thread id =" + Thread.currentThread().getId());
                        return Integer.parseInt(s);
                    }
                })
                .observeOn(Schedulers.from(new Executor() {
                    @Override
                    public void execute(Runnable command) {
                        new Thread(command).start();
                    }
                }))
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        txt_thread_log.setText(txt_thread_log.getText() + "\n" + "Executor thread id =" + Thread.currentThread().getId());
                        Log.i(TAG, "Executor thread id =" + Thread.currentThread().getId());
                        return "6" + integer;
                    }
                })
                .observeOn(Schedulers.computation())
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        txt_thread_log.setText(txt_thread_log.getText() + "\n" + "Schedulers.computation() thread id =" + Thread.currentThread().getId());
                        Log.i(TAG, "Schedulers.computation() thread id =" + Thread.currentThread().getId());
                        return Integer.parseInt(s);
                    }
                })
                .observeOn(Schedulers.immediate())
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        txt_thread_log.setText(txt_thread_log.getText() + "\n" + "Schedulers.immediate() thread id =" + Thread.currentThread().getId());
                        Log.i("Tiny", "Schedulers.immediate() thread id =" + Thread.currentThread().getId());
                        return "7" + integer;
                    }
                })
                .observeOn(Schedulers.trampoline())
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        txt_thread_log.setText(txt_thread_log.getText() + "\n" + "Schedulers.trampoline() thread id =" + Thread.currentThread().getId());
                        Log.i(TAG, "Schedulers.trampoline() thread id =" + Thread.currentThread().getId());
                        return Integer.parseInt(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber1);
    }

    Subscriber<Integer> subscriber1 = new Subscriber<Integer>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(Integer integer) {
            txt_thread_log.setText(txt_thread_log.getText() + "\n" + "Tiny AndroidSchedulers.mainThread() thread id =" + Thread.currentThread().getId() + "\n" + "compose --" + integer);
            Log.i("Tiny", "AndroidSchedulers.mainThread() thread id =" + Thread.currentThread().getId());
            Log.i("Tiny", "compose --" + integer);
        }
    };
}
