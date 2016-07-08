package cc.lijingbo.zhihudemo.model.api;

import cc.lijingbo.zhihudemo.bean.ZhiHNewsBean;
import cc.lijingbo.zhihudemo.bean.ThemesBean;
import cc.lijingbo.zhihudemo.bean.ZhiHContentBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ZhiHApi {

  //最新消息：http://news-at.zhihu.com/api/4/news/latest
  @GET("/api/4/news/{latest}") Call<ZhiHNewsBean> getLatestNews(@Path("latest") String name);

  //消息内容获取与离线下载:http://news-at.zhihu.com/api/4/news/3892357,id为最新消息获取到的id
  @GET("/api/4/news/{id}") Call<String> getNewsDetails(@Path("id") int id);

  //过往消息:http://news-at.zhihu.com/api/4/news/before/20130520，时间,不能小于20130519，否是返回空

  @GET("/api/4/news/before/{time}") Call<ZhiHNewsBean> getBeforeNews(@Path("time") int time);

  //http://news-at.zhihu.com/api/4/themes
  @GET("/api/4/{themes}") Call<ThemesBean> getThemes(@Path("themes") String themes);

  //http://news-at.zhihu.com/api/4/news/3892357
  @GET("/api/4/news/{id}") Call<ZhiHContentBean> getZhiHContent(@Path("id") int id);
}
