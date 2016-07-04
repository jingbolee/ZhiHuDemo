package cc.lijingbo.zhihudemo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cc.lijingbo.zhihudemo.R;
import cc.lijingbo.zhihudemo.bean.ThemesBean;
import java.util.List;

public class ZhiHThemesAdapter extends BaseAdapter {
  List<ThemesBean.Theme> mList;
  Context mContext;

  public ZhiHThemesAdapter(Context context, List<ThemesBean.Theme> themes) {
    mList = themes;
    mContext = context;
  }

  @Override public int getCount() {
    return mList.size() == 0 ? 0 : mList.size() + 1;
  }

  @Override public ThemesBean.Theme getItem(int position) {
    return mList.get(position - 1);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    View view;
    if (position == 0) {
      view = LayoutInflater.from(mContext).inflate(R.layout.nav_menu_list_home, parent, false);
    } else {
      ThemesBean.Theme theme = this.getItem(position);
      view = LayoutInflater.from(mContext).inflate(R.layout.nav_menu_list_item, parent, false);
      TextView textNavMenuListItem = (TextView) view;
      textNavMenuListItem.setText(theme.getName());
    }
    return view;
  }
}
