package cc.lijingbo.zhihudemo;

import android.app.Application;

public class ZhiHuApp extends Application {

  private static ZhiHuApp mInstance;

  @Override public void onCreate() {
    super.onCreate();
    mInstance = this;
  }

  public static ZhiHuApp getInstance() {
    return mInstance;
  }
}
