package tk.dongyeblog.rxjavademo.base;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * description: 所有activity的基类
 * author: dongyeforever@foxmail.com
 * date: 2015-08-20 15:22
 * version: 1.0
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean needRefresh = false;
    private BroadcastReceiver receiver;

    private CompositeSubscription mCompositeSubscription;
    protected Subscription subscription;

    public CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        return this.mCompositeSubscription;
    }

    //添加到订阅
    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onBeforeSetContentLayout();
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);

        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }

        // 通过注解绑定控件
        ButterKnife.bind(this);
        init(savedInstanceState);
        initView();
        initData();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        //取消所有订阅
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
        unsubscribe();
    }

    protected void onBeforeSetContentLayout() {
    }

    protected int getLayoutId() {
        return 0;
    }

    protected View inflateView(int resId) {
        return getLayoutInflater().inflate(resId, null);
    }

    protected void init(Bundle savedInstanceState) {
    }

    //跳转到Activity
    public void toActivity(Class<?> activity) {
        startActivity(new Intent(this, activity));
    }

    //跳转到Activity 传递参数bundle
    public void toActivity(Class<?> activity, Bundle bundle) {
        Intent i = new Intent(this, activity);
        i.putExtras(bundle);
        startActivity(i);
    }

    //跳转到Activity
    public void toActivity(Class<?> activity, String key, String value) {
        Intent i = new Intent(this, activity);
        i.putExtra(key, value);
        startActivity(i);
    }

    //跳转到Activity
    public void toActivity(Class<?> activity, String key, int value) {
        Intent i = new Intent(this, activity);
        i.putExtra(key, value);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {

    }

    protected void initView() {
    }

    protected void initData() {
    }

    //联网自动刷新
    protected void refresh() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    protected void addFragment(@IdRes int containerId, Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(containerId, fragment, tag);
        transaction.commit();
    }

    public void replaceFragment(@IdRes int id_content, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id_content, fragment);
        transaction.commit();
    }

    public class NetWorkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int networkStatus = intent.getIntExtra("networkStatus", 0);
            if (needRefresh && networkStatus > 0) {
                //刷新
                refresh();
            }
        }
    }

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

/*    public void showDialog() {
        //加载中动画
        loadingDialog = new LoadingDialog(this, R.style.LoadingDialog);
        loadingDialog.show();
    }

    public void cancelDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
    }*/

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
