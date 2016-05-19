package tk.dongyeblog.rxjavademo.base;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import tk.dongyeblog.rxjavademo.util.ImagePipelineConfigFactory;

public class App extends Application {
    private static App instance;
    private static Context context;
    /**
     * Log or request TAG
     */
    public static final String TAG = App.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        Fresco.initialize(this, ImagePipelineConfigFactory.getOkHttpImagePipelineConfig(this));
    }

    public static synchronized App getInstance() {
        return instance;
    }

    /*
     * 获取到全局的context
     */
    public static Context getContext() {
        return context;
    }

}
