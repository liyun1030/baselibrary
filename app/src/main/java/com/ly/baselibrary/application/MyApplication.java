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
