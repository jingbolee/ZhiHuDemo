package cc.lijingbo.zhihudemo.ui.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lijingbo.zhihudemo.R;
import cc.lijingbo.zhihudemo.bean.LatestNewsBean;
import cc.lijingbo.zhihudemo.global.Global;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ZhiHLatestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final String TAG = "ZhiHLatestAdapter";
  private static final int TYPE_HEADER = 0;
  private static final int TYPE_ITEM = 1;
  private static final int TYPE_DATE = 2;
  Context mContext;
  List<LatestNewsBean.StoryBean> storyBeanList;
  List<LatestNewsBean.TopStoryBean> topStoryBeanList;

  public ZhiHLatestAdapter(Context context, List<LatestNewsBean.TopStoryBean> topStoryList,
      List<LatestNewsBean.StoryBean> storyList) {
    mContext = context;
    storyBeanList = storyList;
    topStoryBeanList = topStoryList;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    if (viewType == TYPE_ITEM) {
      view =
          LayoutInflater.from(mContext).inflate(R.layout.recyclerview_latest_item, parent, false);
      return new ItemViewHolder(view);
    } else if (viewType == TYPE_HEADER) {
      view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_banner, parent, false);
      return new HeaderViewHolder(view);
    } else if (viewType == TYPE_DATE) {
      view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_date, parent, false);
      return new DateViewHolder(view);
    }
    return null;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ItemViewHolder) {
      ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
      LatestNewsBean.StoryBean storyBean = storyBeanList.get(position - 1);
      if (null != storyBean.getImages()) {
        Picasso.with(mContext)
            .load(storyBean.getImages()[0])
            .placeholder(R.drawable.noimage)
            .into(itemViewHolder.latestImage);
      }
      itemViewHolder.latestTitle.setText(storyBean.getTitle());
    }
    if (holder instanceof HeaderViewHolder) {
      HeaderViewHolder headerViewHoler = (HeaderViewHolder) holder;
      if (topStoryBeanList.size() > 0) {
        TopStoriesPagerAdapter pagerAdapter =
            new TopStoriesPagerAdapter(mContext, topStoryBeanList);
        headerViewHoler.banner.setAdapter(pagerAdapter);
      }
    }
    if (holder instanceof DateViewHolder) {
      DateViewHolder dateViewHolder = (DateViewHolder) holder;
      int date = mContext.getSharedPreferences(Global.SHAREP_NAME, Context.MODE_PRIVATE)
          .getInt(Global.UPDATE_TIME, 0);
      if (date > 0) {
        dateViewHolder.textRecyclerViewDate.setText("日期:" + date);
      }
    }
  }

  @Override public int getItemCount() {
    return storyBeanList.size() == 0 ? 0 : storyBeanList.size() + 1;
  }

  @Override public int getItemViewType(int position) {
    if (position == 0) {
      return TYPE_HEADER;
    } else if (position == 1) {
      return TYPE_DATE;
    } else {
      return TYPE_ITEM;
    }
  }

  class ItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.latest_image) ImageView latestImage;
    @BindView(R.id.latest_title) TextView latestTitle;

    public ItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  class HeaderViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.banner_viewpager) ViewPager banner;

    public HeaderViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  class DateViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.text_recyclerview_date) TextView textRecyclerViewDate;

    public DateViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
