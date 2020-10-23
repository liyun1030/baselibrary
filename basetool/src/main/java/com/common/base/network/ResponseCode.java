package com.common.base.network;

/**
 * 需要显示的错误码提示
 */
public class ResponseCode {

    public static final String TIME_OUT = "-0001";
    public static final String TIME_OUT_MSG = "网络超时";

    /**
     * 公共
     */
    //无用户ID信息
    public static final String NO_USER_ID = "0001";
    public static final String NO_USER_ID_MSG = "请登录";

    //未知错误
    public static final String GET_USER_INFO_FAIL = "0002";
    public static final String GET_USER_INFO_FAIL_MSG = "系统出错";

    //获取POST信息出错
    public static final String GET_POST_INFO_FAIL = "0003";
    public static final String GET_POST_INFO_FAIL_MSG = "请登录";

    //用户未登录
    public static final String NO_USER_SESSION_COUNT = "0005";
    public static final String NO_USER_SESSION_COUNT_MSG = "请登录";

    //该功能已取消
    public static final String FUNCTION_ABANDON = "0010";
    public static final String FUNCTION_ABANDON_MSG = "该功能已取消";

    //请升级到最新版
    public static final String NEED_UPDATE = "0011";
    public static final String NEED_UPDATE_MSG = "请升级到最新版";

    //权限不足
    public static final String AUTHORITY_DENY = "0012";
    public static final String AUTHORITY_DENY_MSG = "权限不足";


    //request token签名参数错误
    public static final String RESPONSE_STATUS_TOKENARGSERROR = "0001";//签名错误
    public static final String RESPONSE_STATUS_TOKENARGSERROR_MSG = "登录状态已失效";

    //request token签名错误
    public static final String RESPONSE_STATUS_SIGNERROR = "0002";//签名错误
    public static final String RESPONSE_STATUS_SIGNERROR_MSG = "登录状态已失效";

    //request token格式错误
    public static final String RESPONSE_STATUS_TOKENFORMATERROR = "0003";//token格式错误
    public static final String RESPONSE_STATUS_TOKENFORMATERROR_MSG = "登录状态已失效";

    //token错误 失效或者不存在
    public static final String RESPONSE_STATUS_TOKENWRONG = "0004";
    public static final String RESPONSE_STATUS_TOKENWRONG_MSG = "登录状态已失效";

    public static final int REQUEST_SUCCESS=2000;
}
