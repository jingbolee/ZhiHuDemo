package cc.lijingbo.zhihudemo.presenter;

public interface IZhiHPresenter {

  void getZhiHLatest();
  void getZhiHBefore(int date);
  void getZhiHFromCache();
  void getZhiHFromCache(int date);
  void getThemes();


}
