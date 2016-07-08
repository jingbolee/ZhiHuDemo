package cc.lijingbo.zhihudemo.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import cc.lijingbo.zhihudemo.ZhiHuApp;
import cc.lijingbo.zhihudemo.bean.ZhiHNewsBean;
import cc.lijingbo.zhihudemo.bean.ThemesBean;
import cc.lijingbo.zhihudemo.global.Global;
import cc.lijingbo.zhihudemo.model.IZhiHRequest;
import cc.lijingbo.zhihudemo.model.ZhiHRequest;
import cc.lijingbo.zhihudemo.ui.activity.IMainActivity;
import com.google.gson.Gson;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZhiHPresenter implements IZhiHPresenter {

  IZhiHRequest request;
  Context mContext;
  IMainActivity iMainActivity;
  RecyclerView mView;
  private SharedPreferences mPref;

  public ZhiHPresenter(IMainActivity iMainActivity, RecyclerView view) {
    this.iMainActivity = iMainActivity;
    mView = view;
    request = new ZhiHRequest();
    mContext = ZhiHuApp.getInstance();
    mPref = mContext.getSharedPreferences(Global.SHAREP_NAME, Context.MODE_PRIVATE);
  }

  @Override public void getZhiHLatest() {
    iMainActivity.showProgressDialog();
    Call<ZhiHNewsBean> call = request.getZhiHNews().getLatestNews("latest");
    call.enqueue(new Callback<ZhiHNewsBean>() {
      @Override public void onResponse(Call<ZhiHNewsBean> call, Response<ZhiHNewsBean> response) {
        if (response.isSuccessful()) {
          ZhiHNewsBean body = response.body();
          Snackbar.make(mView, "刷新成功", Snackbar.LENGTH_SHORT).show();
          mPref.edit().putString(Global.LATEST_JSON, new Gson().toJson(body)).commit();
          int date = body.getDate();
          mPref.edit().putInt(Global.UPDATE_TIME, date).commit();
          iMainActivity.updateAppBarTitle("日期：" + date);
          iMainActivity.updateCurrentDate(date);
          List<ZhiHNewsBean.StoryBean> stories = body.getStories();
          List<ZhiHNewsBean.TopStoryBean> topStories = body.getTop_stories();
          iMainActivity.updateListData(stories, topStories);
          iMainActivity.hideProgressDialog();
        }
      }

      @Override public void onFailure(Call<ZhiHNewsBean> call, Throwable t) {
        Snackbar.make(mView, "网络异常", Snackbar.LENGTH_SHORT).show();
        iMainActivity.hideProgressDialog();
        getZhiHFromCache();
        Log.e("Demo", t.getMessage());
      }
    });
  }

  @Override public void getZhiHBefore(final int time) {
    iMainActivity.startLoadMoreStates();
    final Call<ZhiHNewsBean> beforeNews = request.getZhiHNews().getBeforeNews(time);
    beforeNews.enqueue(new Callback<ZhiHNewsBean>() {
      @Override public void onResponse(Call<ZhiHNewsBean> call, Response<ZhiHNewsBean> response) {
        if (response.isSuccessful()) {
          ZhiHNewsBean body = response.body();
          int date = body.getDate();
          mPref.edit().putString(String.valueOf(date), new Gson().toJson(body)).commit();
          iMainActivity.updateCurrentDate(date);
          List<ZhiHNewsBean.StoryBean> storyBeen = body.getStories();
          iMainActivity.loadMore(storyBeen);
          iMainActivity.stopLoadMoreStates();
        }
      }

      @Override public void onFailure(Call<ZhiHNewsBean> call, Throwable t) {
        iMainActivity.stopLoadMoreStates();
        getZhiHFromCache(time);
      }
    });
  }

  @Override public void getZhiHFromCache() {
    String json = mPref.getString(Global.LATEST_JSON, null);
    if (null != json) {
      ZhiHNewsBean bean = new Gson().fromJson(json, ZhiHNewsBean.class);
      int date = bean.getDate();
      iMainActivity.updateAppBarTitle("日期：" + date);
      iMainActivity.updateCurrentDate(date);
      List<ZhiHNewsBean.StoryBean> stories = bean.getStories();
      List<ZhiHNewsBean.TopStoryBean> topStories = bean.getTop_stories();
      iMainActivity.updateListData(stories, topStories);
    }
  }

  @Override public void getZhiHFromCache(int time) {
    String json = mPref.getString(String.valueOf(time), null);
    if (null != json) {
      ZhiHNewsBean bean = new Gson().fromJson(json, ZhiHNewsBean.class);
      int date = bean.getDate();
      iMainActivity.updateCurrentDate(date);
      List<ZhiHNewsBean.StoryBean> stories = bean.getStories();
      iMainActivity.loadMore(stories);
    }
  }

  @Override public void getThemes() {
    Call<ThemesBean> themes = request.getZhiHNews().getThemes("themes");
    themes.enqueue(new Callback<ThemesBean>() {
      @Override public void onResponse(Call<ThemesBean> call, Response<ThemesBean> response) {
        if (response.isSuccessful()) {
          ThemesBean body = response.body();
          SharedPreferences sharedPreferences =
              mContext.getSharedPreferences(Global.SHAREP_NAME, Context.MODE_PRIVATE);
          sharedPreferences.edit().putString(Global.THEMES, new Gson().toJson(body)).commit();
          List<ThemesBean.Theme> others = body.getOthers();
          iMainActivity.updateThemesData(others);
        }
      }

      @Override public void onFailure(Call<ThemesBean> call, Throwable t) {
        SharedPreferences sharedPreferences =
            mContext.getSharedPreferences(Global.SHAREP_NAME, Context.MODE_PRIVATE);
        String body = sharedPreferences.getString(Global.THEMES, null);
        if (null != body) {
          ThemesBean themesBean = new Gson().fromJson(body, ThemesBean.class);
          List<ThemesBean.Theme> others = themesBean.getOthers();
          iMainActivity.updateThemesData(others);
        }
      }
    });
  }
}
