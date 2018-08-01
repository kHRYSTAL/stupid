package me.khrystal.util.spiderman;

import android.content.Context;
import android.content.Intent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/8/1
 * update time:
 * email: 723526676@qq.com
 */
public class SpiderMan implements Thread.UncaughtExceptionHandler {

    private static SpiderMan spiderMan = new SpiderMan();

    private Context mContext;
    private Thread.UncaughtExceptionHandler mExceptionHandler;

    private Builder mBuilder;

    private SpiderMan() {}

    public static SpiderMan getInstance() {
        return spiderMan;
    }

    public Builder init(Context context) {
        this.mContext = context;
        mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mBuilder = new Builder();
        return mBuilder;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (mBuilder == null) return;
        CrashModel model = parseCrash(throwable);
        if (mBuilder.mOnCrashListener != null) {
            mBuilder.mOnCrashListener.onCrash(thread, throwable, model);
        }
        if (mBuilder.mEnable) {
            handleException(model);
        } else {
            if (mExceptionHandler != null) {
                mExceptionHandler.uncaughtException(thread, throwable);
            }
        }
    }

    private void handleException(CrashModel model) {
        if (mBuilder.mEnable && mBuilder.mShowCrashMessage) {
            Intent intent = new Intent(mContext, CrashActivity.class);
            intent.putExtra(CrashActivity.CRASH_MODEL, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public interface OnCrashListener {
        void onCrash(Thread t, Throwable ex, CrashModel model);
    }

    private CrashModel parseCrash(Throwable ex) {
        CrashModel model = new CrashModel();
        model.setEx(ex);
        model.setTime(new Date().getTime());
        StringBuilder msgBuilder = new StringBuilder();
        String exceptionMsg = null;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        pw.flush();
        String exceptionType = ex.getClass().getName();
        exceptionMsg = ex.getMessage();
        msgBuilder.append(ex.getMessage());
        msgBuilder.append("\n");
        if (ex.getStackTrace() != null && ex.getStackTrace().length > 0) {
            StackTraceElement element = ex.getStackTrace()[0];
            model.setExceptionMsg(exceptionMsg);
            model.setLineNumber(element.getLineNumber());
            model.setClassName(element.getClassName());
            model.setFileName(element.getFileName());
            model.setMethodName(element.getMethodName());
            model.setExceptionType(exceptionType);
        }
        model.setFullException(sw.toString());
        return model;
    }

    public class Builder {
        private boolean mEnable;
        private boolean mShowCrashMessage;
        private OnCrashListener mOnCrashListener;

        public Builder setEnable(boolean enable) {
            this.mEnable = enable;
            return this;
        }

        public Builder showCrashMessage(boolean show) {
            this.mShowCrashMessage = show;
            return this;
        }

        public void setOnCrashListener(OnCrashListener listener) {
            this.mOnCrashListener = listener;
        }
    }
}
