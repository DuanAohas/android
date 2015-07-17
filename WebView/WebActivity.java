package com.mrocker.salon.app.customer.ui.activity.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.Toast;

import com.mrocker.library.util.CheckUtil;
import com.mrocker.library.util.Lg;
import com.mrocker.library.util.StringUtil;
import com.mrocker.library.util.ToastUtil;
import com.mrocker.salon.app.R;
import com.mrocker.salon.app.base.BaseActivity;
import com.mrocker.salon.app.base.SalonCfg;
import com.mrocker.salon.app.customer.entity.BannerEntity;
import com.mrocker.salon.app.customer.entity.WebH5Entity;
import com.mrocker.salon.app.customer.ui.activity.bespeak.PayMessage;
import com.mrocker.salon.app.net.SalonLoading;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import org.json.JSONObject;


/**
 * Created by aaa on 2015/2/11 0011.
 */
public class WebActivity extends BaseActivity {
    public static final String TITLE = "title";
    public static final String WEB_URL = "web_url";
    public static final String WEB_IMG = "web_img";
    public static final String WEB_ENTRY = "web_entry";
    private WebView wb_act_web;
    private String title = "";
    private String url = "";//
    private String webImg = "";
//    private String externJsonString = "";
    private BannerEntity.ExtraMsg extraMsg = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_web);

        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA);
//        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT);

    }


    @Override
    protected void initHeader() {
        showLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        showTitle(getString(R.string.web_info));
    }

    private UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    private void doShare(String shareTitle, String shareDesc, String shareContent, String shareUrl, String shareImgUrl) {
//        ToastUtil.debug("分享");
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, SalonCfg.WEIXIN_APPID, SalonCfg.WEIXIN_APPSECRET);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, SalonCfg.WEIXIN_APPID, SalonCfg.WEIXIN_APPSECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, SalonCfg.QQ_APPID, SalonCfg.QQ_KEY);
        qqSsoHandler.addToSocialSDK();
//mController.setAppWebSite("http://mp.weixin.qq.com/s?__biz=MzA3MzM3Mjk4Nw==&mid=210800060&idx=1&sn=3112edf485ae23ec16a7d07e1698a5e8&scene=1&key=af154fdc40fed0038c2ac2592f23a24bc3c46c3fef9583918a780ffab37b4075ae9e2c669907f22c6d2f7c1a811a9d83&ascene=0&uin=MTY5NTM3MTU%3D&devicetype=iMac+MacBookPro12%2C1+OSX+OSX+10.10.3+build(14D136)&version=11020012&pass_ticket=hLYbkw5mMOxmzik%2FYyQgA0wlfgXJe%2FTzVwL%2B6103JTQ%3D");
        // 设置分享内容
        if (CheckUtil.isEmpty(shareTitle))
            shareTitle = "南瓜车美发 - 不办卡不推销!";
        if (CheckUtil.isEmpty(shareContent) && CheckUtil.isEmpty(shareDesc)) {
            shareContent = shareTitle;
        }
        if(CheckUtil.isEmpty(shareDesc))
            shareDesc = shareContent;
        if (CheckUtil.isEmpty(shareContent))
            shareContent = shareDesc;

//        String shareTitle = "南瓜车美发 - 不办卡不推销!";//+productEntity.name;//productEntity.intro;//http://test.nanguache.com/ngcmweb/go.html?goApp=productId%7C55559afd0cf26a71107bb488
//        String shareUrl = url;//SalonLoading.SERVER_WEB_URL+"/go.html?goApp=productId_" + productEntity.productID;//'"http://a.app.qq.com/o/simple.jsp?pkgname=com.mrocker.salon.app";;//"sharenanguache://data?productID="+productEntity.productID;//"http://a.app.qq.com/o/simple.jsp?pkgname=com.mrocker.salon.app";//"http://mp.weixin.qq.com/s?__biz=MzA3MzM3Mjk4Nw==&mid=210800060&idx=1&sn=3112edf485ae23ec16a7d07e1698a5e8&scene=1";
//        mController.setAppWebSite(SHARE_MEDIA.RENREN, shareUrl);
//        mController.setAppWebSite(SHARE_MEDIA.SINA, shareUrl);
        mController.setAppWebSite(shareUrl);
        wxHandler.setTitle(shareTitle);
        wxHandler.setTargetUrl(shareUrl);
        wxCircleHandler.setTitle(shareTitle);
        wxCircleHandler.setTargetUrl(shareUrl);
        qqSsoHandler.setTitle(shareTitle);
        qqSsoHandler.setTargetUrl(shareUrl);
////        if(CheckUtil.isEmpty(extraMsg)) {
//            wxHandler.setTitle(shareTitle);
//            wxHandler.setTargetUrl(shareUrl);
////        } else {
////            wxHandler.setTitle(shareTitle);
////            wxHandler.setTargetUrl(shareDesc + shareUrl);
////        }
////        if(CheckUtil.isEmpty(extraMsg)) {
//            wxCircleHandler.setTitle(shareContent);
//            wxCircleHandler.setTargetUrl(shareUrl);
////        }else{
////            wxCircleHandler.setTitle(shareTitle);
////            wxCircleHandler.setTargetUrl(shareContent+shareUrl);
////        }
//            qqSsoHandler.setTitle(shareTitle + shareDesc);
//            qqSsoHandler.setTargetUrl(shareUrl);

        mController.setShareContent(shareDesc);//+shareUrl
        if (CheckUtil.isEmpty(shareImgUrl))
            mController.setShareMedia(new UMImage(this, R.drawable.ic_launcher));
        else
            mController.setShareMedia(new UMImage(this, /*shareImgUrl */SalonLoading.getImageUrl(shareImgUrl, 100, 100)));
        // 设置分享图片, 参数2为图片的url地址
        mController.openShare(this, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                if (i == StatusCode.ST_CODE_SUCCESSED) {
                    Toast.makeText(WebActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
//                    Lg.d("share", "========分享成功=======");
                } else {
                    Toast.makeText(WebActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
//                    Lg.d("share", "========分享失败=======eCode===>" + i);
                }
            }
        });
    }

    @Override
    protected void initWidget() {
        wb_act_web = (WebView) findViewById(R.id.wb_act_web);
    }

    @Override
    protected void setWidgetState() {
        getData();
        if (!CheckUtil.isEmpty(title) && (!title.equals("美发价目表") && !title.equals("意见反馈"))) {
            showRightButton(R.drawable.common_title_right_share_selector, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(CheckUtil.isEmpty(extraMsg)){
                        doShare("", "", "", url, webImg);
                    }else {
                        doShare(extraMsg.shareTitle, extraMsg.shareDesc, "",extraMsg.shareLink, extraMsg.shareImg);
                    }
                }
            });
        }
    }

    private void getData() {
        url = getIntent().getStringExtra(WEB_URL).toString();
        title = getIntent().getStringExtra(TITLE);
        webImg = getIntent().getStringExtra(WEB_IMG);
//        externJsonString = getIntent().getStringExtra(WEB_ENTRY);
//        if (!CheckUtil.isEmpty(externJsonString)){
//            extraMsg = BannerEntity.ExtraMsg.getObjectData(externJsonString);
//        }
//        if (!CheckUtil.isEmpty(externJsonString)){
        extraMsg= (BannerEntity.ExtraMsg)getIntent().getSerializableExtra(WEB_ENTRY);
        if (CheckUtil.isEmpty(title)) {
            title = "南瓜车";//showTitle(title);
        }
        showTitle(title);
        if (CheckUtil.isEmpty(url))
            url = "http://www.nanguache.com/";

        if (url.indexOf("/ngcmweb/") >= 0) {// 判断是否要添加token
            //getWebTokenStr
            url = SalonLoading.getInstance().token.getWebTokenStr(url);
        }
        setData();
    }

    private void setData() {
        Lg.d("setWidgetState", "获取的url是:" + url);
        if (!StringUtil.isEmpty(url)) {
//            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
//            startActivity(intent);
//            finish();

            wb_act_web.setHorizontalScrollBarEnabled(false);
            wb_act_web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            WebSettings settings = wb_act_web.getSettings();
            settings.setJavaScriptEnabled(true); //设置js权限，比如js弹出窗
            settings.setSupportMultipleWindows(true);
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setDomStorageEnabled(true);
            settings.setPluginState(WebSettings.PluginState.ON);
            settings.setUserAgentString("ngcCustomerApp" + settings.getUserAgentString());
//            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
//            settings.setBlockNetworkImage(true);//把图片加载放在最后来加载渲染
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setAllowFileAccess(true);// 设置允许访问文件数据
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            settings.setDatabaseEnabled(true);
            wb_act_web.setDownloadListener(new HWebViewDownLoadListener());//添加页面中的下载功能
//            settings.setPluginState(WebSettings.PluginState.ON);
//            settings.setUseWideViewPort(true);
//            settings.setLoadWithOverviewMode(true);

//            wb_act_web.getSettings().setUserAgentString("ngcCustomerApp"+wb_act_web.getSettings().getUserAgentString());
//            mWebView.getSettings().setUserAgentString(ua);
//            mWebView.getSettings().getUserAgentString();
            //webview只是一个承载体，各种内容的渲染需要使用webviewChromClient去实现，所以set一个默认的基类WebChromeClient就行，
            wb_act_web.setWebChromeClient(new WebChromeClient());
            wb_act_web.addJavascriptInterface(new AndroidWebJs(), "ngcApp");//在 安桌17以上才可以用
            Log.d("getUserAgentString ", " .getSettings().getUserAgentString():" + wb_act_web.getSettings().getUserAgentString());
            //设置标题栏名称使用方法 在js中调用如：  ngcApp.setWebViewTitle(“评论列表”);  JavaScriptInterface.setWebViewTitle(“资讯页”);
//            Lg.d("startProgressBar", "1获取的url是:" + url);
//            startProgressBar(R.string.progreessBar_wait, true, false, null);
            wb_act_web.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    WebView.HitTestResult hit = view.getHitTestResult();//自动跳转处理
                    if (hit.getExtra() != null) {
                        skipWebActivity(0, "南瓜车", url, "",null);

                    } else {
                        WebActivity.this.url = url;
                        view.loadUrl(url);
                    }
                    return true;
//                    if (url.indexOf("lotteryxuancaiios://") != -1) {
////                        setResult(RESULT_OK);
//                        //lotteryxuancaiios://action=212&code=0&orderid=
//                        //action:21&1:31&2:38code:32orderid:39
//                        Log.d("返回的数据有:", "url:" + url);
//                        finish();
//                        return true;
//                    }
//                    if (url.indexOf("sharenanguache://") >=0) {//跳转到自己应用
//                        shareDataMsg.jumpToActivity(WebActivity.this,url);
////                        shareDataMsg.getUrlMsg(url);
////                        if (shareDataMsg.isFromData) {
////                            shareDataMsg.isFromData = shareDataMsg.shareJumpActivity(WebActivity.this);
////                        }
//                        if (shareDataMsg.isFromData)// 如果执行了就返回操作
//                            return true;
//                    }
//                    if( url.startsWith("http:") || url.startsWith("https:") ) {
//                        return false;
//                    }
//                    {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity( intent );
//                    }
//                    return true;

//                    view.loadUrl(url);//当前网页的链接仍在webView中跳转
//                    return false;
                }

                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
//                    if(view.getProgress()<100){
//                        Log.d("testTimeout", "timeout...........");
//                    }
                }

                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    closeProgressBar();
                    String title = view.getTitle();
                    if (CheckUtil.isEmpty(title)) return;
//                    view.getTitle(); 加载完成后获取 html5页面title
//                    if (!CheckUtil.isEmpty(title))
//                        showTitle(title);
                    if(CheckUtil.isEmpty(WebActivity.this.title) || WebActivity.this.title.compareTo("南瓜车") ==0){
                        WebActivity.this.title = title;
                        showTitle(title);
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                    Log.d("error", view + ":" + errorCode + ";" + ":" + description + ":" + failingUrl);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
//                    handler.proceed();//表示等待证书响应
//                    handler.cancel();//表示挂起连接，为默认方式
//                    handler.handleMessage(null);//可做其他处理
                    Log.d("error", "" + error);
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    super.onLoadResource(view, url);
                }
            });

            wb_act_web.loadUrl(url);

//            wb_act_web.goBack();   //后退
//            wb_act_web.goForward();//前进
//            wb_act_web.reload();  //刷新
        }
    }

    private class HWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    private final int WEBJS_TITLE = 1;
    private final int WEBJS_SHARE = 2;
    private final int WEBJS_SHARE_SHOW = 3;
    private final int WEBJS_SHARE_OBJ = 4;
    private android.os.Handler handler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WEBJS_TITLE:
                    if (!CheckUtil.isEmpty(handlerTitle))
                        WebActivity.this.showTitle(handlerTitle);//
                    break;
                case WEBJS_SHARE: {//shareTextJson  undefined
                    if (CheckUtil.isEmpty(shareTextJson) || shareTextJson.compareTo("undefined") == 0) {
//                        ToastUtil.toast("分享内容为空！");
                        break;
                    }
                    webH5Entity = WebH5Entity.getData(shareTextJson);
                    // doShare(webH5Entity.title,webH5Entity.desc,webH5Entity.content,webH5Entity.url,webH5Entity.icon);
                }
                break;
                case WEBJS_SHARE_SHOW: {
                    int shtype = (int) msg.arg1;
                    WebActivity.this.showRightButton(shtype == 1 ? true : false);
                }
                break;
                case WEBJS_SHARE_OBJ: {
                    if (CheckUtil.isEmpty(shareTextJsonObj)) {
//                        ToastUtil.toast("分享内容为空！" + shareTextJsonObj.toString());
                        break;
                    }
                    webH5Entity = WebH5Entity.getData(shareTextJsonObj.toString());
                    doShare(webH5Entity.title, webH5Entity.desc, webH5Entity.content, webH5Entity.url, webH5Entity.icon);

                }
                break;
            }
        }
    };

    private WebH5Entity webH5Entity = null;
    private String handlerTitle = "南瓜车";
    private String shareTextJson = "";
    private JSONObject shareTextJsonObj = new JSONObject();

    public class AndroidWebJs {
        public AndroidWebJs() {
        }

        //设置顶部标题
        @JavascriptInterface
        public void setWebViewTitle(String title) {
            Message msg = new Message();
            msg.what = WEBJS_TITLE;
            handlerTitle = title;
            handler.sendMessage(msg);
        }

        @JavascriptInterface
        public void setAPPViewShareShow(boolean br) {// 设置是否显示分享功能
//            if (br){
            Message msg = new Message();
            msg.what = WEBJS_SHARE_SHOW;
            handlerTitle = title;
            msg.arg1 = (br ? 1 : 0);
            handler.sendMessage(msg);

//            }
        }

        @JavascriptInterface
        public void setAppViewShare(JSONObject strJson) {//doShare 分享
//            if (CheckUtil.isEmpty(strJson))
            Lg.i("share obj " + strJson.toString());
            Message msg = new Message();
            msg.what = WEBJS_SHARE_OBJ;
            shareTextJsonObj = strJson;
            handler.sendMessage(msg);
        }

        @JavascriptInterface
        public void setAppViewShare(String strJson) {//doShare 分享
//            if (CheckUtil.isEmpty(strJson))
            Lg.i("share strJson " + strJson);
            Message msg = new Message();
            msg.what = WEBJS_SHARE;
            shareTextJson = strJson;
            handler.sendMessage(msg);
//            webH5Entity = WebH5Entity.getData(strJson);
//            doShare(webH5Entity.title,webH5Entity.desc,webH5Entity.content,webH5Entity.url,webH5Entity.icon);
        }

        @JavascriptInterface
        public void setAppViewShare(String title, String url, String img) {//doShare 分享
//            doShare(title,"","",url,img);
            Lg.i("share url " + url);
        }

        @JavascriptInterface
        public String webStringToHtml() {
            return null;
        }
    }
}
