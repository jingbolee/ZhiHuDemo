package cc.lijingbo.zhihudemo.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import cc.lijingbo.zhihudemo.ZhiHuApp;
import cc.lijingbo.zhihudemo.bean.LatestNewsBean;
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

  public ZhiHPresenter(IMainActivity iMainActivity, RecyclerView view) {
    this.iMainActivity = iMainActivity;
    mView = view;
    request = new ZhiHRequest();
    mContext= ZhiHuApp.getInstance();
  }

  @Override public void getZhiHLatest() {
    iMainActivity.showProgressDialog();
    Call<LatestNewsBean> call = request.getZhiHNews().getLatestNews("latest");
    call.enqueue(new Callback<LatestNewsBean>() {
      @Override
      public void onResponse(Call<LatestNewsBean> call, Response<LatestNewsBean> response) {
        if (response.isSuccessful()) {
          LatestNewsBean body = response.body();
          Snackbar.make(mView,"刷新成功",Snackbar.LENGTH_SHORT).show();
          SharedPreferences sharedPreferences =
              mContext.getSharedPreferences(Global.SHAREP_NAME, Context.MODE_PRIVATE);
          sharedPreferences.edit().putString(Global.LATEST_JSON, new Gson().toJson(body)).commit();
          int date = body.getDate();
          iMainActivity.updateAppBarTitle("日期：" + date);
          List<LatestNewsBean.StoryBean> stories = body.getStories();
          iMainActivity.updateListData(stories);
          iMainActivity.hideProgressDialog();
        }
      }

      @Override public void onFailure(Call<LatestNewsBean> call, Throwable t) {
        Snackbar.make(mView,"网络异常",Snackbar.LENGTH_LONG).setAction("将会加载本地数据",
            new View.OnClickListener() {
              @Override public void onClick(View v) {
                iMainActivity.hideProgressDialog();
              }
            }).show();
        iMainActivity.hideProgressDialog();
        getZhiHFromCache();
        Log.e("Demo", t.getMessage());
      }
    });
  }

  @Override public void getZhiHFromCache() {
    SharedPreferences sharedPreferences =
        mContext.getSharedPreferences(Global.SHAREP_NAME, Context.MODE_PRIVATE);
    String body = sharedPreferences.getString(Global.LATEST_JSON, null);
    if (null != body) {
      LatestNewsBean bean = new Gson().fromJson(body, LatestNewsBean.class);
      int date = bean.getDate();
      iMainActivity.updateAppBarTitle("日期：" + date);
      List<LatestNewsBean.StoryBean> stories = bean.getStories();
      iMainActivity.updateListData(stories);
    }
  }
}
