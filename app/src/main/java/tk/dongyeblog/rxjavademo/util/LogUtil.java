package tk.dongyeblog.rxjavademo.util;

import android.util.Log;

public class LogUtil {
	public static final int VERBOSE = 2;
	public static final int DEBUG = 3;
	public static final int INFO = 4;
	public static final int WARN = 5;
	public static final int ERROR = 6;
	public static final int NOTHING = 7;
	
	public static final int LEVEL = VERBOSE;

	public static void v(String msg) {
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		if (LEVEL <= VERBOSE) {
			Log.v(tag, msg);
		}
	}

	public static void d(String msg) {
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		if (LEVEL<=DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void e(String msg) {
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		if (LEVEL <=ERROR) {
			Log.e(tag, msg);
		}
	}

	public static void i(String msg) {
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		if (LEVEL <=INFO) {
			Log.i(tag, msg);
		}
	}

	public static void w(String msg) {
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		if (LEVEL <=WARN) {
			Log.w(tag, msg);
		}
	}

	private static String generateTag(StackTraceElement caller) {
		String tag = "%s.%s(L:%d)";
		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName
				.lastIndexOf(".") + 1);
		tag = String.format(tag, callerClazzName, caller.getMethodName(),
				caller.getLineNumber());
		return tag;
	}

	public static StackTraceElement getCurrentStackTraceElement() {
		return Thread.currentThread().getStackTrace()[3];
	}

	public static StackTraceElement getCallerStackTraceElement() {
		return Thread.currentThread().getStackTrace()[4];
	}

}
