package cc.lijingbo.zhihudemo.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lijingbo.zhihudemo.R;
import cc.lijingbo.zhihudemo.bean.ThemesBean;
import cc.lijingbo.zhihudemo.bean.ZhiHNewsBean;
import cc.lijingbo.zhihudemo.global.Global;
import cc.lijingbo.zhihudemo.presenter.IZhiHPresenter;
import cc.lijingbo.zhihudemo.presenter.ZhiHPresenter;
import cc.lijingbo.zhihudemo.ui.adapter.ZhiHLatestAdapter;
import cc.lijingbo.zhihudemo.ui.adapter.ZhiHThemesAdapter;
import cc.lijingbo.zhihudemo.utils.DensityUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
    implements IMainActivity, SwipeRefreshLayout.OnRefreshListener,
    NavigationView.OnNavigationItemSelectedListener {

  @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
  @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout mSwipeRefreshLayout;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.drawerlayout) DrawerLayout mDrawerlayout;
  //@BindView(R.id.nav_view) NavigationView mNavView;
  @BindView(R.id.nav_list_view) ListView mNavListView;
  private Unbinder mBind;
  private IZhiHPresenter iZhiHPresenter;
  private List<ZhiHNewsBean.StoryBean> storiesList = new ArrayList<>();
  private List<ZhiHNewsBean.TopStoryBean> topStoriesList = new ArrayList<>();
  private List<ThemesBean.Theme> mThemesList = new ArrayList<>();
  private Set<String> mReaderSet = new HashSet<>();
  private ZhiHLatestAdapter mAdapter;
  private ZhiHThemesAdapter mNavListAdapter;
  private int mClickPosition = 0;
  private SharedPreferences mPref;
  private int mCurrentDate = 20130519;
  private boolean isLoading = false;
  private boolean isRefresh = false;

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
    int[] devicePx = DensityUtil.getDevicePx(this);
    mPref = getSharedPreferences(Global.SHAREP_NAME, MODE_PRIVATE);
    SharedPreferences.Editor edit = mPref.edit();
    edit.putInt(Global.DEVICE_WIDTH, devicePx[0]);
    edit.putInt(Global.DEVICE_HEIGTH, devicePx[1]);
    edit.commit();
    Set<String> readerItem = mPref.getStringSet(Global.READER_ITEM, null);
    if (null != readerItem) {
      mReaderSet.addAll(readerItem);
    }
  }

  private void initView() {
    mSwipeRefreshLayout.setOnRefreshListener(this);
    mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light, android.R.color.holo_orange_light,
        android.R.color.holo_red_light);
    mAdapter = new ZhiHLatestAdapter(this, topStoriesList, storiesList, mReaderSet);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        if (lastVisibleItemPosition == mAdapter.getItemCount() - 1) {
          Log.e("Demo", "加载更多");
          if (!isLoading && !isRefresh) {
            if (mCurrentDate != 20130519) {
              iZhiHPresenter.getZhiHBefore(mCurrentDate);
            } else {
              Snackbar.make(mRecyclerView, "我的生日是2013年5月19日，所以以前的日报是空的", Snackbar.LENGTH_LONG)
                  .show();
            }
          }
        }
      }
    });
    mAdapter.setOnItemClickListener(new ZhiHLatestAdapter.OnItemClickListener() {
      @Override public void itemClick(View v, int id, int position) {
        mClickPosition = position;
        mReaderSet.add(String.valueOf(id));
        mAdapter.notifyDataSetChanged();
        Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        overridePendingTransition(R.anim.main_enter_anim,R.anim.main_exit_anim);
      }
    });
    mNavListAdapter = new ZhiHThemesAdapter(this, mThemesList);
    View view = LayoutInflater.from(this).inflate(R.layout.nav_header, null, false);
    mNavListView.addHeaderView(view);
    mNavListView.setAdapter(mNavListAdapter);
    onRefresh();

    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, mDrawerlayout, mToolbar, R.string.drawer_open,
            R.string.drawer_close);
    toggle.syncState();
    mDrawerlayout.addDrawerListener(toggle);
  }

  @Override protected void onStop() {
    mPref.edit().putStringSet(Global.READER_ITEM, mReaderSet).commit();
    super.onStop();
  }

  @Override protected void onDestroy() {
    mBind.unbind();
    super.onDestroy();
  }

  @Override public void updateCurrentDate(int date) {
    mCurrentDate = date;
  }

  @Override public void updateAppBarTitle(String name) {
    mToolbar.setTitle(name);
  }

  @Override public void showProgressDialog() {
    if (isRefresh) mSwipeRefreshLayout.setRefreshing(true);
  }

  @Override public void updateListData(List<ZhiHNewsBean.StoryBean> stories,
      List<ZhiHNewsBean.TopStoryBean> topStories) {
    storiesList.clear();
    topStoriesList.clear();
    storiesList.addAll(stories);
    topStoriesList.addAll(topStories);
    mAdapter.notifyDataSetChanged();
    mRecyclerView.scrollToPosition(mClickPosition);
  }

  @Override public void updateThemesData(List<ThemesBean.Theme> list) {
    mThemesList.clear();
    mThemesList.addAll(list);
    mNavListAdapter.notifyDataSetChanged();
  }

  @Override public void loadMore(List<ZhiHNewsBean.StoryBean> stories) {
    storiesList.addAll(stories);
    mAdapter.notifyDataSetChanged();
  }

  @Override public void hideProgressDialog() {
    if (isRefresh) {
      mSwipeRefreshLayout.setRefreshing(false);
      isRefresh = false;
    }
  }

  @Override public void stopLoadMoreStates() {
    isLoading = false;
  }

  @Override public void startLoadMoreStates() {
    isLoading = true;
  }

  @Override public void onRefresh() {
    if (!isRefresh) {
      iZhiHPresenter.getZhiHLatest();
      getSlideThemes();
      mClickPosition = 0;
      isRefresh = true;
    }
  }

  public void getSlideThemes() {
    iZhiHPresenter.getThemes();
  }

  @Override public boolean onNavigationItemSelected(MenuItem item) {
    return false;
  }
}
