package cc.lijingbo.zhihudemo.ui.activity;

import cc.lijingbo.zhihudemo.bean.LatestNewsBean;
import cc.lijingbo.zhihudemo.bean.ThemesBean;
import java.util.List;

public interface IMainActivity {

  void updateAppBarTitle(String name);

  void showProgressDialog();

  void hideProgressDialog();

  void updateListData(List<LatestNewsBean.StoryBean> stories);

  void updateThemesData(List<ThemesBean.Theme> list);
}
