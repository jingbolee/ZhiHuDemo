package cc.lijingbo.zhihudemo.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import cc.lijingbo.zhihudemo.bean.LatestNewsBean;
import cc.lijingbo.zhihudemo.model.IZhiHRequest;
import cc.lijingbo.zhihudemo.model.ZhiHRequest;
import cc.lijingbo.zhihudemo.ui.activity.IMainActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZhiHPresenter implements IZhiHPresenter {

  IZhiHRequest request;
  Context mContext;
  IMainActivity iMainActivity;
  Activity mActivity;

  public ZhiHPresenter(IMainActivity iMainActivity, Activity activity) {
    this.iMainActivity = iMainActivity;
    mActivity = activity;
    request = new ZhiHRequest();
  }

  @Override public void getZhiHLatest() {
    iMainActivity.showProgressDialog();
    Call<LatestNewsBean> call = request.getZhiHNews().getLatestNews("latest");
    call.enqueue(new Callback<LatestNewsBean>() {
      @Override
      public void onResponse(Call<LatestNewsBean> call, Response<LatestNewsBean> response) {
        if (response.isSuccessful()) {
          LatestNewsBean body = response.body();
          int date = body.getDate();
          iMainActivity.updateAppBarTitle("日期："+date);
          List<LatestNewsBean.StoryBean> stories = body.getStories();
          iMainActivity.updateListData(stories);
          iMainActivity.hideProgressDialog();
        }
      }

      @Override public void onFailure(Call<LatestNewsBean> call, Throwable t) {
        Toast.makeText(mActivity,t.getMessage(),Toast.LENGTH_SHORT).show();
        Log.e("Demo",t.getMessage());
      }
    });
  }
}
