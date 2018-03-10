package cc.lijingbo.zhihudemo.utils;

import java.io.File;

import cc.lijingbo.zhihudemo.ZhiHuApp;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * @Author: Li Jingbo
 * @Date: 2016-07-12 20:45
 */
public class OkHttpUtils {

    private static final String TAG = "OkHttpUtils";

    static volatile OkHttpClient singleton = null;
    static File cacheFile;
    static Cache cache;

    static {
        cacheFile = new File(ZhiHuApp.getInstance().getCacheDir() + File.separator + "ZhiHLatest");
        cache = new Cache(cacheFile, 50 * 1024 * 1024);
    }

    public static OkHttpClient getInstance() {
        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {
                    singleton = new OkHttpClient.Builder().cache(cache).build();
                }
            }
        }
        return singleton;
    }
}
