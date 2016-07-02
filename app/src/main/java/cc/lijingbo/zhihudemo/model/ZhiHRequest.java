package cc.lijingbo.zhihudemo.model;

import cc.lijingbo.zhihudemo.model.api.ZhiHApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author: Li Jingbo
 * @Date: 2016-07-01 11:48
 */
public class ZhiHRequest implements IZhiHRequest {

  @Override public ZhiHApi getZhiHNews() {
    ZhiHApi zhiHApi = new Retrofit.Builder().baseUrl("http://news-at.zhihu.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ZhiHApi.class);

    return zhiHApi;
  }
}
