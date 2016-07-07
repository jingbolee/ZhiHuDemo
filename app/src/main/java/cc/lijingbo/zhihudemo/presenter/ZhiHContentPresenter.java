package cc.lijingbo.zhihudemo.presenter;

import cc.lijingbo.zhihudemo.bean.ZhiHContentBean;
import cc.lijingbo.zhihudemo.model.IZhiHRequest;
import cc.lijingbo.zhihudemo.model.ZhiHRequest;
import cc.lijingbo.zhihudemo.ui.activity.iContentActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZhiHContentPresenter implements IZhiHContentPresenter {

  IZhiHRequest request;
  iContentActivity mActivity;

  public ZhiHContentPresenter(iContentActivity activity) {
    request = new ZhiHRequest();
    mActivity = activity;
  }

  @Override public void getZhiHContent(int id) {
    Call<ZhiHContentBean> zhiHContent = request.getZhiHNews().getZhiHContent(id);
    zhiHContent.enqueue(new Callback<ZhiHContentBean>() {
      @Override
      public void onResponse(Call<ZhiHContentBean> call, Response<ZhiHContentBean> response) {
        if (response.isSuccessful()) {
          ZhiHContentBean body = response.body();
          mActivity.showContent(body);
        }
      }

      @Override public void onFailure(Call<ZhiHContentBean> call, Throwable t) {

      }
    });
  }
}
