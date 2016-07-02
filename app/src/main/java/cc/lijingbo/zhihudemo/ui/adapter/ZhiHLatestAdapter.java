package cc.lijingbo.zhihudemo.ui.adapter;

import android.content.Context;
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
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * @Author: Li Jingbo
 * @Date: 2016-07-01 12:37
 */
public class ZhiHLatestAdapter extends RecyclerView.Adapter<ZhiHLatestAdapter.ViewHolder> {
  private static final String TAG = "ZhiHLatestAdapter";
  Context mContext;
  List<LatestNewsBean.StoryBean> list;

  public ZhiHLatestAdapter(Context context, List<LatestNewsBean.StoryBean> beanList) {
    mContext = context;
    list = beanList;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(mContext).inflate(R.layout.recyclerview_latest_item, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    LatestNewsBean.StoryBean storyBean = list.get(position);
    if (null != storyBean.getImages()) {
      Picasso.with(mContext)
          .load(storyBean.getImages()[0])
          .placeholder(R.drawable.noimage)
          .into(holder.latestImage);
    }
    holder.latestTitle.setText(storyBean.getTitle());
  }

  @Override public int getItemCount() {
    return list.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.latest_image) ImageView latestImage;
    @BindView(R.id.latest_title) TextView latestTitle;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
