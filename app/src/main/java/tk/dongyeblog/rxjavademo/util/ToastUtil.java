package tk.dongyeblog.rxjavademo.util;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import tk.dongyeblog.rxjavademo.base.App;

/**
 * description: toast工具类
 * author： dongyeforever@gmail.com
 * date: 2016-01-29 13:33
 */
public class ToastUtil {
    private static Toast mToast;
    private static MyHandler baseHandler = new MyHandler();

    public static void sendMessage(int flag) {
        baseHandler.sendEmptyMessage(flag);
    }

    public static void showToast(String toastMessage) {
        baseHandler.toastMessage = toastMessage;
        sendMessage(MyHandler.SHOW_STR_TOAST);
    }

    public static void showToast(int res) {
        baseHandler.toastRes = res;
        sendMessage(MyHandler.SHOW_RES_TOAST);
    }

    private static class MyHandler extends Handler {
        public static final int SHOW_STR_TOAST = 0;
        public static final int SHOW_RES_TOAST = 1;

        private String toastMessage;
        private int toastRes;

        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case SHOW_STR_TOAST:
                        if (mToast == null) {
                            mToast = Toast.makeText(App.getContext(), toastMessage, Toast.LENGTH_SHORT);
                        } else {
                            mToast.setText(toastMessage);
                        }
                        mToast.show();
                        break;
                    case SHOW_RES_TOAST:
                        if (mToast == null) {
                            mToast = Toast.makeText(App.getContext(), toastRes, Toast.LENGTH_SHORT);
                        } else {
                            mToast.setText(toastMessage);
                        }
                        mToast.show();
                        break;
                }
            }
        }
    }
}
