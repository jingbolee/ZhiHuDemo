package cc.lijingbo.zhihudemo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lijingbo.zhihudemo.R;
import cc.lijingbo.zhihudemo.bean.ZhiHContentBean;
import cc.lijingbo.zhihudemo.global.Global;
import cc.lijingbo.zhihudemo.presenter.IZhiHContentPresenter;
import cc.lijingbo.zhihudemo.presenter.ZhiHContentPresenter;
import cc.lijingbo.zhihudemo.utils.ConverterString;
import cc.lijingbo.zhihudemo.utils.DensityUtil;
import cc.lijingbo.zhihudemo.utils.HtmlSanitizer;
import com.squareup.picasso.Picasso;
import java.io.IOException;

public class ContentActivity extends AppCompatActivity implements iContentActivity {
  private Unbinder mBind;
  IZhiHContentPresenter presenter;
  //@BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.content_image) ImageView contentImage;
  @BindView(R.id.webview) WebView webview;
  @BindView(R.id.content_text) TextView contentText;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_content2);
    mBind = ButterKnife.bind(this);
    presenter = new ZhiHContentPresenter(this);
    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (null != actionBar) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    WebSettings settings = webview.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setSupportZoom(false);
    settings.setJavaScriptCanOpenWindowsAutomatically(true);
    //settings.setAllowUniversalAccessFromFileURLs(true);
    settings.setDefaultTextEncodingName("UTF-8");
    webview.setWebViewClient(new WebViewClient() {

                               @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                 return false;
                               }
                             }

    );

    int id = getIntent().getIntExtra("id", 0);
    if (id != 0) {
      presenter.getZhiHContent(id);
    }
  }

  @Override protected void onDestroy() {
    mBind.unbind();
    super.onDestroy();
  }

  @Override public void showContent(ZhiHContentBean bean) {
    int anInt =
        getSharedPreferences(Global.SHAREP_NAME, MODE_PRIVATE).getInt(Global.DEVICE_WIDTH, 0);
    int height = DensityUtil.dip2px(ContentActivity.this, 210);
    Picasso.with(ContentActivity.this)
        .load(bean.getImage())
        .resize(anInt, height)
        .centerCrop()
        .into(contentImage);
    contentText.setText(bean.getTitle());
    //api返回的不是一个标准的html格式，需要拼接
    String content =
        "<html><head><meta name=\"viewport\" content=\"target-densitydpi=device-dpi,width=device-width,initial-scale=1.0\"/>";

    try {
      String css =
          ConverterString.InputStream2String(getAssets().open("news_qa.auto.css"), "UTF-8");
      content += "<style type=\"text/css\">" + css + "</style>";
      content += "</head><body>" + bean.getBody() + "</body></html>";
    } catch (IOException e) {
      e.printStackTrace();
    }
    String sanitizedContent = HtmlSanitizer.sanitize(content);
    webview.loadDataWithBaseURL("http://", sanitizedContent, "text/html", "UTF-8", null);
  }
}