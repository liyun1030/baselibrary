package com.ly.baselibrary.application;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.StrictMode;

import androidx.core.content.ContextCompat;

import com.common.base.BuildConfig;
import com.common.base.application.BaseApplication;
import com.common.base.network.ACache;
import com.common.base.network.TokenInterceptor;
import com.common.base.network.config.CookiesManager;
import com.common.base.network.config.NetworkManager;
import com.common.base.network.config.NetworkManagerConfiguration;
import com.common.base.tool.AppBuildConfig;
import com.common.base.tool.BaseConstant;
import com.common.base.tool.CommUtils;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ly.baselibrary.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.model.HttpHeaders;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.widget.GFImageView;
import cn.finalteam.toolsfinal.StorageUtils;
import okhttp3.OkHttpClient;

public class MyApplication extends BaseApplication {

    private static MyApplication instance = null;

    private ACache mCache;

    /**
     * App是否在前台
     */
    private boolean isActive;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public String getSpName() {
        return CommUtils.getPkgName(this)+BaseConstant.SP_NAME;
    }

    @Override
    public void init() {
        instance = this;
        isActive = true;
        mCache = ACache.get(this);

        initConfig();
        initOkGo();
        initJpush();
        initNetwork();
        initGallery();
        //解决7.0以上下载更新文件问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private void initConfig() {
        AppBuildConfig.DEBUG = BuildConfig.DEBUG;
        AppBuildConfig.APPLICATION_ID = BuildConfig.APPLICATION_ID;
        AppBuildConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
        AppBuildConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
    }

    /**
     * 初始化极光
     */
    private void initJpush() {
//        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);            // 初始化 JPush
//        String registrationID = JPushInterface.getRegistrationID(this);
//        LogUtils.d("jgjg", registrationID);
    }

    private void initOkGo() {
        //公共头部
        HttpHeaders headers = new HttpHeaders();
//        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        //公共参数
//        HttpParams params = new HttpParams();
//        params.put("device_type", "1");     //param支持中文,直接传,不要自己编码
//        params.put("t", System.currentTimeMillis()+"");
//        params.put("token","111");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
//        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.NONE);        //log打印级别，决定了log显示的详细程度
//        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
//        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
//        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
//        builder.addInterceptor(TokenInterceptor.getInstance());

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));     //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
//        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(new SafeHostnameVerifier());
        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
//                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers);                    //全局公共头
//                .addCommonParams(params);                       //全局公共参数
    }

    public void initNetwork() {
        NetworkManagerConfiguration.Builder builder = new NetworkManagerConfiguration.Builder();
        builder.setUserAgent("CureFun", BuildConfig.VERSION_NAME);
        builder.addInterceptor(TokenInterceptor.getInstance(this));
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
            builder.addInterceptor(new StethoInterceptor());
        }
        builder.cookieJar(new CookiesManager());
        NetworkManager.getInstance().config(builder.build());
    }

    private void initGallery() {
        ThemeConfig theme = new ThemeConfig.Builder().setTitleBarBgColor(ContextCompat.getColor(this, R.color.blue)).setCheckSelectedColor(ContextCompat.getColor(this, R.color.blue)).setFabNornalColor(ContextCompat.getColor(this, R.color.blue)).build();
        ImageLoader imageloader = new UILImageLoader();
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, BaseConstant.CACHE_DIR + "images");
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme).setTakePhotoFolder(cacheDir).setEditPhotoCacheFolder(cacheDir).setNoAnimcation(true).build();
        GalleryFinal.init(coreConfig);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    static class UILImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

        private static final long serialVersionUID = -2557823134590140760L;
        private Bitmap.Config mImageConfig;

        public UILImageLoader() {
            this(Bitmap.Config.RGB_565);
        }

        public UILImageLoader(Bitmap.Config config) {
            this.mImageConfig = config;
        }

        @Override
        public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(false)
                    .cacheInMemory(false)
                    .bitmapConfig(mImageConfig)
                    .build();
            ImageSize imageSize = new ImageSize(width, height);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("file://" + path, new ImageViewAware(imageView), options, imageSize, null, null);
        }

        @Override
        public void clearMemoryCache() {

        }

    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端确定
     */
    public class SafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                for (X509Certificate certificate : chain) {
                    certificate.checkValidity(); //检查证书是否过期，签名是否通过等
                }
            } catch (Exception e) {
                throw new CertificateException(e);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么自定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    public class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //验证主机名是否匹配
            //return hostname.equals("server.jeasonlzy.com");
            return true;
        }
    }
}
