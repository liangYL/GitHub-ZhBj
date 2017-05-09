package com.boke.zhbj.base.menudetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.boke.zhbj.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;


/**
 * Created by administrator on 2017/4/19.
 */
public class NewsDetailWebActivity extends Activity implements View.OnClickListener{


    private ImageButton btnBack;
    private ImageButton btnTextSize;
    private ImageButton btnShare;
    private ProgressBar pbLoading;

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_news_detail);
        mWebView = (WebView)findViewById(R.id.wv_webview);

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnTextSize = (ImageButton)findViewById(R.id.btn_textsize);
        btnShare = (ImageButton)findViewById(R.id.btn_share);
        pbLoading = (ProgressBar)findViewById(R.id.pb_loading);

        //按钮设置监听
        btnBack.setOnClickListener(this);
        btnTextSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        String mUrl = getIntent().getStringExtra("url");
        // 加载网页
        mWebView.loadUrl("http://huaban.com/boards/30643621/");

        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
        settings.setUseWideViewPort(true);// 只是双击缩放
        settings.setJavaScriptEnabled(true);// 打开js功能
        //eglCodecCommon: ** ERROR unknown type 0x73000f (glSizeof,80)
        //Genymotion模拟器不支持硬件加速,关闭即可:
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //加载方法很多
        mWebView.setWebViewClient(new WebViewClient(){
            // 网页开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbLoading.setVisibility(View.VISIBLE);
            }
            // 网页跳转
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // <a href="tel:110">联系我们</a>
                // PhoneGap(js和本地代码互动)
                view.loadUrl(url);// 强制在当前页面加载网页, 不用跳浏览器
                return true;
            }
            // 网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            // 加载进度回调
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                System.out.println("newProgress:" + newProgress);
            }
            // 网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                System.out.println("title:" + title);
            }

        });
        //回退功能
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 需要添加 mWv.canGoBack(),不然当返回到初始页面时,可能无法继续通过返回键关闭页面
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
                return false;
            }
        });

    }

    // 点击确定前, 用户选择的字体大小的位置
    private int mChooseItem;
    // 当前的字体位置
    private int mSelectItem = 2;

    /**
     * 选择字体大小的弹窗
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
                "超小号字体" };
        builder.setSingleChoiceItems(items, mSelectItem,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChooseItem = which;
                    }
                });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                WebSettings settings = mWebView.getSettings();

                switch (mChooseItem) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;

                    default:
                        break;
                }

                mSelectItem = mChooseItem;
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 确保SDcard下面存在此张图片
     */
    private void showShare() {
//        Toast.makeText(getApplicationContext(), "敬请期待", Toast.LENGTH_SHORT).show();
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();

        //修改主题
        oks.setTheme(OnekeyShareTheme.fromValue(2));

        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://baidu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("请输入分享文字");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
        oks.setImageUrl("http://img.hb.aicdn.com/f5463e0e3eef66b56494041cae126b67cb5137404c505-l8nENq_fw658");// 确保SDcard下面存在此张图片

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://baidu.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("请输入评论文字");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://baidu.com");

        // 启动分享GUI
        oks.show(this);
   }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_textsize:
                showChooseDialog();
                break;
            case R.id.btn_share:
                showShare();
                break;
            default:
                break;
        }
    }
}
