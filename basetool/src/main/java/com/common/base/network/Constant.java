package com.common.base.network;

import android.Manifest;
import android.text.TextUtils;

import com.common.base.tool.FileUtils;

/**
 * created by 李云 on 2019/6/17
 * 本类的作用:
 */
public class Constant {
    public static final String[] PERMISSIONS =
            {Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final String APP_NAME = "curefun_doctor";
    public static final int TYPE_TOPIC = 1;
    public static final int TYPE_DISCUSSION = 2;
    public static final int LIKE_TYPE_DISLIKE = 0;
    public static final int LIKE_TYPE_LIKE = 1;
    public static final int LIKE_TYPE_NONE = 2;
    public static final String FROM_ABOUT = "from_about";
    public static final String FROM_MAIN = "from_main";
    public static final String USER_ID = "user_id";
    public static String SP_NAME = "curefun_doctor_sp";
    public static String PIC_HOST = "";
    public static String HOST_URL = "admin.scjc.com/api.php/";
    public static final String SP_USER_LOGIN_KEY = "curefun_doctor_user";
    public static final String SP_USER_DETAIL_KEY = "curefun_doctor_user_detail";
    public static final String TOGGLE = "toggle_wifi";
    public static final String UPDATE_BEAN_TAG = "update_info_bean";
    public static final String PBL_APP_DIR = "com.curefun.doctor";
    public static final String PBL_DOWNLOAD_DIR = PBL_APP_DIR + ".download";
    public static final String PBL_CAPTURE_DIR = PBL_APP_DIR + "img";
    public static final String APP_FILE_ROOT_PATH = FileUtils.getRootFilePath() + Constant.PBL_APP_DIR + "/files";//存储本地文件地址
    public static final String SET_UNREAD_COUNT = "SET_UNREAD_COUNT";             //设置消息未读数
    public static final String SET_READED = "SET_READED";                           //设置未读消息为已读
    public static final String USER_AVATAR = "userAvatar";
    public static final String ACCOUNT = "account_name";
    public static final String PASSWORD = "password";
    public static final String STRATEGY="http://educate.scwjxx.cn:8090/html/pri.html";
    public static final String PROTOCAL="http://educate.scwjxx.cn:8090/agreement/contract.html";
    public static final String IS_FIRST="app_is_first";

    /**
     * 账号密码本地存储加密冗余字段
     */
    public static final String BASE64_EXTRA = "talent";
    /**
     * CDN线路
     */
    public static final String CDN_ROUTE_QILIUYUN = "QN";
    public static final String CDN_ROUTE_ALI = "AL";
    public static String CDN_ROUTE_CURRENT = CDN_ROUTE_QILIUYUN;


    /**
     * 七流云cdn前缀
     */
    private static String CDN_QN_PREFIX;
    /**
     * 阿里云cdn前缀
     */
    private static String CDN_AL_PREFIX;

    /**
     * 获取cdn前缀
     *
     * @return
     */
    public static String getCDNPrefix() {
        if (TextUtils.isEmpty(CDN_AL_PREFIX)) {
            if (TextUtils.equals("http://192.168.0.122", getAddress())) {           //本地开发
                CDN_QN_PREFIX = "http://resourcetest.curefun.com/";
            } else if (TextUtils.equals("http://192.168.0.112", getAddress())) {   //本地测试
                CDN_QN_PREFIX = "http://resourcetest.curefun.com/";
            } else if (TextUtils.equals("http://59.173.19.28", getAddress()) || TextUtils.equals("http://101.132.24.138", getAddress())) {   //线上测试  武汉机房
                CDN_QN_PREFIX = "http://cdn.curefun.com/";
            } else {
                CDN_QN_PREFIX = "http://cdn.curefun.com/";                           //线上正式环境
            }
        }
        return CDN_QN_PREFIX;
    }

    public static void setCDNRoute() {
        CDN_ROUTE_CURRENT = CDN_ROUTE_QILIUYUN;
    }

    public static String getCDNRoute() {
        return CDN_ROUTE_CURRENT;
    }

    public static String getAddress() {
//        if (TextUtils.isEmpty(ADDRESS)) {
//            ADDRESS = SharedPreferencesUtils.getParam(BaseApplication.sInstance, SHARE_ADDRESS);
//        }
//        if (TextUtils.isEmpty(ADDRESS)) {   //规避share如果被清理的情况
//            ADDRESS = "http://www.curefun.com";
//            setAddress(ADDRESS);
//        }
        return "http://www.curefun.com";
//        ADDRESS = "http://192.168.0.108:8081";
    }
}
