package cc.lijingbo.zhihudemo.ui.activity;

import cc.lijingbo.zhihudemo.bean.ZhiHNewsBean;
import cc.lijingbo.zhihudemo.bean.ThemesBean;
import java.util.List;

public interface IMainActivity {
  void updateCurrentDate(int date);

  void updateAppBarTitle(String name);

  void showProgressDialog();

  void hideProgressDialog();

  void stopLoadMoreStates();

  void startLoadMoreStates();

  void updateListData(List<ZhiHNewsBean.StoryBean> stories,
      List<ZhiHNewsBean.TopStoryBean> topStories);

  void updateThemesData(List<ThemesBean.Theme> list);

  void loadMore(List<ZhiHNewsBean.StoryBean> stories);
}
