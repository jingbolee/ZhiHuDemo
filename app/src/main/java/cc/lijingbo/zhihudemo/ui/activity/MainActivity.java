package cc.lijingbo.zhihudemo.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lijingbo.zhihudemo.R;
import cc.lijingbo.zhihudemo.bean.LatestNewsBean;
import cc.lijingbo.zhihudemo.presenter.IZhiHPresenter;
import cc.lijingbo.zhihudemo.presenter.ZhiHPresenter;
import cc.lijingbo.zhihudemo.ui.adapter.ZhiHLatestAdapter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements IMainActivity, SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
  @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout mSwipeRefreshLayout;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  private Unbinder mBind;
  private IZhiHPresenter iZhiHPresenter;
  private List<LatestNewsBean.StoryBean> storiesList = new ArrayList<>();
  private ZhiHLatestAdapter mAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setSupportActionBar(mToolbar);
    mBind = ButterKnife.bind(this);
    initData();
    initView();
  }

  private void initData() {
    iZhiHPresenter = new ZhiHPresenter(this, mRecyclerView);
  }

  private void initView() {
    mSwipeRefreshLayout.setOnRefreshListener(this);
    mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light, android.R.color.holo_orange_light,
        android.R.color.holo_red_light);
    mAdapter = new ZhiHLatestAdapter(this, storiesList);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mAdapter);
    onRefresh();
    //iZhiHPresenter.getZhiHLatest();
  }

  @Override protected void onDestroy() {
    mBind.unbind();
    super.onDestroy();
  }

  @Override public void updateAppBarTitle(String name) {
    mToolbar.setTitle(name);
  }

  @Override public void showProgressDialog() {
    if (!mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(true);
  }

  @Override public void updateListData(List<LatestNewsBean.StoryBean> stories) {
    storiesList.clear();
    storiesList.addAll(stories);
    mAdapter.notifyDataSetChanged();
  }

  @Override public void hideProgressDialog() {
    if (mSwipeRefreshLayout.isRefreshing()) {
      mSwipeRefreshLayout.setRefreshing(false);
    }
  }

  @Override public void onRefresh() {
    iZhiHPresenter.getZhiHLatest();
  }
}
