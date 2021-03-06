package com.common.base.network;

import android.Manifest;
import android.text.TextUtils;

import com.common.base.tool.FileUtils;

/**
 * created by 李云 on 2019/6/17
 * 本类的作用:
 */
public class Constant {
    public static String SP_NAME = "ly_library_sp";
    public static String PIC_HOST = "";
    public static String HOST_URL = "admin.scjc.com/api.php/";
    public static final String SP_USER_LOGIN_KEY = "curefun_doctor_user";
    public static final String SP_USER_DETAIL_KEY = "curefun_doctor_user_detail";
    public static final String PBL_APP_DIR = "com.curefun.doctor";

    /**
     * CDN线路
     */
    public static final String CDN_ROUTE_QILIUYUN = "QN";
    public static final String CDN_ROUTE_ALI = "AL";
    public static String CDN_ROUTE_CURRENT = CDN_ROUTE_QILIUYUN;
    public static final String SP_USER_KEY="library_user";

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
