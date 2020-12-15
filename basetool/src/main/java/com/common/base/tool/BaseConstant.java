package com.common.base.tool;

import com.common.base.view.emoji.EmoBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by 李云 on 2018/12/27
 * 本类的作用:基类常量
 */
public class BaseConstant {
    public static  String SP_NAME=".common_sp";//常量名称-外界传入
    public static List<EmoBean> emoBeans;
    public static final String CACHE_DIR = "curefun_doctor/";
    public static final int CLICK_CHECK = 200;//点击事件间隔
    public static final String APP_NAME = "curefun_doctor";



    //-------------------banner跳转类型--------------------------------------
    public static final String VIDEO="1";//课程

    public static final String TOPIC="2";//题组

    public static final String CASE="3";//病例	第三方链接

    public static final String NEWS="4";//新闻

    public static final String DOCUMENT="5";//文档
    /**
     * 获取类型标记.
     *
     * @param type
     * @return
     */
    protected static Map<String, Integer> flagMap = new HashMap<>();
    public static String LOGIN_LOGIN_BY_ACCOUNT = "http://192.168.1.222/szhiqu/uapi/login/loginByPsw";
    public static int LOGIN_LOGIN_BY_ACCOUNT_FLAG=1;
    public Integer getFlag(String type) {
        return flagMap.get(type);
    }
    public static final int GET_OVERVIEW_STARNDARD_TRAIN_EXAM_FLAG = 715;
    static {
        flagMap.put(LOGIN_LOGIN_BY_ACCOUNT, LOGIN_LOGIN_BY_ACCOUNT_FLAG);
    }


}
