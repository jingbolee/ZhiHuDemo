package cc.lijingbo.zhihudemo.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity implements IMainActivity,SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.progressbar) ProgressBar progressBar;
  @BindView(R.id.recyclerview) RecyclerView recyclerView;
  @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.toolbar) Toolbar toolbar;
  private Unbinder mBind;
  private IZhiHPresenter iZhiHPresenter;
  private List<LatestNewsBean.StoryBean> storiesList = new ArrayList<>();
  private ZhiHLatestAdapter mAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setSupportActionBar(toolbar);
    mBind = ButterKnife.bind(this);
    initData();
    initView();
  }

  private void initData() {
    iZhiHPresenter = new ZhiHPresenter(this, this);
  }

  private void initView() {
    mAdapter = new ZhiHLatestAdapter(this, storiesList);
    recyclerView.setHasFixedSize(true);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(mAdapter);
    iZhiHPresenter.getZhiHLatest();
  }

  @Override protected void onDestroy() {
    mBind.unbind();
    super.onDestroy();
  }

  @Override public void updateAppBarTitle(String name) {
    toolbar.setTitle(name);
  }

  @Override public void showProgressDialog() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override public void updateListData(List<LatestNewsBean.StoryBean> stories) {
    storiesList.clear();
    storiesList.addAll(stories);
    mAdapter.notifyDataSetChanged();
  }

  @Override public void hideProgressDialog() {
    progressBar.setVisibility(View.INVISIBLE);
  }

  @Override public void onRefresh() {

  }
}
