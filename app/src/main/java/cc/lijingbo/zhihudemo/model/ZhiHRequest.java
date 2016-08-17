package cc.lijingbo.zhihudemo.model;

import cc.lijingbo.zhihudemo.model.api.ZhiHApi;
import cc.lijingbo.zhihudemo.utils.OkHttpUtils;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author: Li Jingbo
 * @Date: 2016-07-01 11:48
 */
public class ZhiHRequest implements IZhiHRequest {

  @Override public ZhiHApi getZhiHNews() {
    OkHttpClient okHttpClient = OkHttpUtils.getInstance();
    return  new Retrofit.Builder().baseUrl("http://news-at.zhihu.com")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ZhiHApi.class);
  }
}
