package base.share;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.common.base.R;
import com.common.base.activity.QQShareActivity;
import com.common.base.tools.AndroidUtil;
import com.common.base.tools.CommUtils;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;

import java.util.ArrayList;
import java.util.List;


/**
 * QQ分享工具类
 */
public class QQUtils {

    /**
     * 分享网络图片地址给QQ朋友
     */
    public static void shareEstimateImageToFriend(Activity activity, String address, String appName) {
        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, address);
        bundle.putInt(ShareConstants.TYPE, ShareConstants.TYPE_IMAGE);
        bundle.putInt(ShareConstants.TO, ShareConstants.TO_FRIEND);
        startActivity(activity, bundle);
    }

    /**
     * 分享网络图片到QQ空间
     */
    public static void shareImageToQZone(Activity activity, String address, String appName) {
        Bundle bundle = new Bundle();
        bundle.putInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
        bundle.putString(QzonePublish.PUBLISH_TO_QZONE_APP_NAME, appName);
        ArrayList<String> images = new ArrayList<>();
        images.add(address);
        bundle.putStringArrayList(QzonePublish.PUBLISH_TO_QZONE_IMAGE_URL, images);
        bundle.putInt(ShareConstants.TYPE, ShareConstants.TYPE_IMAGE);
        bundle.putInt(ShareConstants.TO, ShareConstants.PUBLISH_QZONE);
        startActivity(activity, bundle);
    }

    /**
     * 分享文字到QQ好友
     */
    public static void shareToQQ(Activity activity, String title, String summary, String imgUrl, String webUrl) {
        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,imgUrl);
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, webUrl);
//        bundle.putInt(ShareConstants.TYPE, ShareConstants.TYPE_CASE);
        bundle.putInt(ShareConstants.TO, ShareConstants.TO_FRIEND);
        startActivity(activity, bundle);
    }

    /**
     * 分享文字到Q空间
     * @param activity
     */
    public static void shareCaseToQZone(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, "");
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "");
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "");
        ArrayList<String> images = new ArrayList<>();
//        images.add(ShareConstants.APP_DOWNLOAD_ICON_ADDRESS);//icon
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
//        bundle.putInt(ShareConstants.TYPE, QQShareActivity.TYPE_CASE);
//        bundle.putInt(ShareConstants.TO, ShareConstants.TO_QZONE);
        startActivity(activity, bundle);
    }

    /**
     * 分享资讯文章
     *带头像，网址，标题
     */
    public static void shareInfoArticleToFriend(Activity activity, String address, String title, String newsIntro, String imageAddress) {
        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, newsIntro);
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, address);
//        bundle.putInt(ShareConstants.TYPE, ShareConstants.TYPE_INFO_ARTICLE);
        bundle.putInt(ShareConstants.TO, ShareConstants.TO_FRIEND);
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageAddress);
        startActivity(activity, bundle);
    }

    private static void startActivity(Activity activity, Bundle bundle) {
        if (!checkQQInstall(activity.getApplicationContext())) {
            CommUtils.showToast(activity.getApplicationContext(), R.string.qq_no_installed);
        } else {
            AndroidUtil.openActivity(activity, QQShareActivity.class, bundle);
        }
    }

    private static boolean checkQQInstall(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
