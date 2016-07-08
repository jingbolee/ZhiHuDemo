package cc.lijingbo.zhihudemo.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import cc.lijingbo.zhihudemo.R;
import cc.lijingbo.zhihudemo.bean.ZhiHNewsBean;
import cc.lijingbo.zhihudemo.global.Global;
import cc.lijingbo.zhihudemo.utils.DensityUtil;
import com.squareup.picasso.Picasso;
import java.util.List;

public class TopStoriesPagerAdapter extends PagerAdapter {
  List<ZhiHNewsBean.TopStoryBean> topStoriesList;
  Context mContext;
  int deviceWidth;
  int imageHeigth;
  OnPagerClickListener mListener;

  public void setOnPagerClickListener(OnPagerClickListener listener) {
    mListener = listener;
  }

  public interface OnPagerClickListener {
    void onPagerClick(View v,int id);
  }

  public TopStoriesPagerAdapter(Context context,
      List<ZhiHNewsBean.TopStoryBean> topStoryBeanList) {
    topStoriesList = topStoryBeanList;
    mContext = context;
    deviceWidth = mContext.getSharedPreferences(Global.SHAREP_NAME, Context.MODE_PRIVATE)
        .getInt(Global.DEVICE_WIDTH, 0);
    imageHeigth = DensityUtil.dip2px(mContext, 200);
  }

  @Override public int getCount() {
    return topStoriesList.size();
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item, container, false);
    final ZhiHNewsBean.TopStoryBean topStoryBean = topStoriesList.get(position);
    ImageView imageView = (ImageView) view;
    Picasso.with(mContext)
        .load(topStoryBean.getImage())
        .resize(deviceWidth, imageHeigth)
        .centerCrop()
        .into(imageView);
    container.addView(view);
    view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mListener.onPagerClick(v,topStoryBean.getId());
      }
    });
    return view;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }
}
