package cc.lijingbo.zhihudemo.ui.activity;

import cc.lijingbo.zhihudemo.bean.LatestNewsBean;
import java.util.List;

/**
 * @Author: Li Jingbo
 * @Date: 2016-07-01 11:54
 */
public interface IMainActivity {

  void updateAppBarTitle(String name);
  void showProgressDialog();
  void hideProgressDialog();
  void updateListData(List<LatestNewsBean.StoryBean> stories);

}
