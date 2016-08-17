package cc.lijingbo.zhihudemo.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lijingbo.zhihudemo.R;
import cc.lijingbo.zhihudemo.bean.ZhiHContentBean;
import cc.lijingbo.zhihudemo.global.Constants;
import cc.lijingbo.zhihudemo.presenter.IZhiHContentPresenter;
import cc.lijingbo.zhihudemo.presenter.ZhiHContentPresenter;
import cc.lijingbo.zhihudemo.utils.ConverterString;
import cc.lijingbo.zhihudemo.utils.DensityUtil;
import cc.lijingbo.zhihudemo.utils.HtmlSanitizer;

public class ContentActivity extends AppCompatActivity implements iContentActivity {
    private Unbinder mBind;
    IZhiHContentPresenter presenter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.content_image)
    ImageView contentImage;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.content_text)
    TextView contentText;
    @BindView(R.id.text_copyright)
    TextView copyRigthText;
    @BindView(R.id.frame_text_background)
    RelativeLayout frameTextBackGround;
    @BindView(R.id.nsv)
    NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content2);
        mBind = ButterKnife.bind(this);

        presenter = new ZhiHContentPresenter(this);
        int id = getIntent().getIntExtra("id", 0);
        if (id != 0) {
            presenter.getZhiHContent(id);
        }
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.content_enter_anim, R.anim.content_exit_anim);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mBind.unbind();
        if (webview != null) {
            ((ViewGroup) webview.getParent()).removeView(webview);
            webview.destroy();
            webview = null;
        }
        super.onDestroy();
    }

    @Override
    public void showFrameTextBackGround() {
        if (scrollView != null) {
            if (View.VISIBLE != scrollView.getVisibility()) {
                scrollView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void hideFrameTextBackGround() {
        if (scrollView != null) {
            if (View.INVISIBLE != scrollView.getVisibility()) {
                scrollView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void showContent(ZhiHContentBean bean) {
        int anInt =
                getSharedPreferences(Constants.SHAREP_NAME, MODE_PRIVATE).getInt(Constants.DEVICE_WIDTH, 0);
        int height = DensityUtil.dip2px(ContentActivity.this, 210);
        Picasso.with(ContentActivity.this)
                .load(bean.getImage())
                .resize(anInt, height)
                .centerCrop()
                .into(contentImage);
        contentText.setText(bean.getTitle());
        copyRigthText.setText(bean.getImage_source());
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
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("UTF-8");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            }
        });

        webview.loadDataWithBaseURL("http://", sanitizedContent, "text/html", "UTF-8", null);
    }


}
