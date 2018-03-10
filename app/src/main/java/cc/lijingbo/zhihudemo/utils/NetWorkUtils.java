package cc.lijingbo.zhihudemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtils {

    private static final String TAG = "NetWorkUtils";

    //判断是否有网络
    public static boolean isNetWorkConnected(Context context) {
        boolean isNetwork = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            isNetwork = networkInfo.isConnectedOrConnecting();
        }
        return isNetwork;
    }

    /**
     * 判断当前网络是 WIFI 还是 mobile
     * -1：没有网络
     * 0：移动
     * 1：WIFI
     */

    public static int getNetWordType(Context context) {
        int networkType = -1; //默认没有网络
        if (isNetWorkConnected(context)) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo.getType() == ConnectivityManager
                    .TYPE_WIFI || activeNetworkInfo.getType() ==
                    ConnectivityManager.TYPE_WIMAX) {
                networkType = 1;// WIFI
            } else if (activeNetworkInfo.getType() == ConnectivityManager
                    .TYPE_MOBILE) {
                networkType = 0; //移动网络
            }
        }
        return networkType;
    }
}
