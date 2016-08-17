package cc.lijingbo.zhihudemo.presenter;

import com.google.gson.Gson;

import java.io.IOException;

import cc.lijingbo.zhihudemo.ZhiHuApp;
import cc.lijingbo.zhihudemo.bean.ZhiHContentBean;
import cc.lijingbo.zhihudemo.model.IZhiHRequest;
import cc.lijingbo.zhihudemo.model.ZhiHRequest;
import cc.lijingbo.zhihudemo.ui.activity.iContentActivity;
import cc.lijingbo.zhihudemo.utils.CacheUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZhiHContentPresenter implements IZhiHContentPresenter {

  IZhiHRequest request;
  iContentActivity mActivity;
  CacheUtils cacheUtils;
  public ZhiHContentPresenter(iContentActivity activity) {
    request = new ZhiHRequest();
    mActivity = activity;
    cacheUtils = CacheUtils.build(ZhiHuApp.getInstance());
  }

  @Override public void getZhiHContent(final int id) {

    String data = cacheUtils.loadData(String.valueOf(id));

    if (data != null) {
      ZhiHContentBean bean = new Gson().fromJson(data, ZhiHContentBean.class);
      mActivity.showFrameTextBackGround();
      mActivity.showContent(bean);
      return;
    }
    Call<ZhiHContentBean> zhiHContent = request.getZhiHNews().getZhiHContent(id);
    zhiHContent.enqueue(new Callback<ZhiHContentBean>() {
      @Override
      public void onResponse(Call<ZhiHContentBean> call, Response<ZhiHContentBean> response) {
        if (response.isSuccessful()) {
          ZhiHContentBean body = response.body();
          try {
            String json = cacheUtils.addData(String.valueOf(id), new Gson().toJson(body));
            ZhiHContentBean zhiHContentBean = new Gson().fromJson(json, ZhiHContentBean.class);
            mActivity.showFrameTextBackGround();
            mActivity.showContent(zhiHContentBean);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      @Override public void onFailure(Call<ZhiHContentBean> call, Throwable t) {
        mActivity.hideFrameTextBackGround();
      }
    });
  }
}
