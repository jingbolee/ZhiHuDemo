package cc.lijingbo.zhihudemo.model;

import cc.lijingbo.zhihudemo.model.api.ZhiHApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZhiHContentRequest implements IZhiHRequest {
  private static final String TAG = "ZhiHContentRequest";

  @Override public ZhiHApi getZhiHNews() {
    return  new Retrofit.Builder().baseUrl("http://news-at.zhihu.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ZhiHApi.class);
  }
}
