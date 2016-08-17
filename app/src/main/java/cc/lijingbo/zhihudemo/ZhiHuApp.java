package cc.lijingbo.zhihudemo;

import android.app.Application;

import com.socks.library.KLog;

public class ZhiHuApp extends Application {

    private static ZhiHuApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        KLog.init(BuildConfig.LOG_DEBUG, "ZhiHuDemo");
    }

    public static ZhiHuApp getInstance() {
        return mInstance;
    }
}
