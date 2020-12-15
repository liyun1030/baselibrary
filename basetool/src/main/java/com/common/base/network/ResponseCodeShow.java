package com.common.base.network;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 需要显示的错误码提示
 */
public class ResponseCodeShow {

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

    //数据升级，请重新创建病人
    public static final String DATA_UPDATE = "0016";
    public static final String DATA_UPDATE_MSG = "数据升级，请重新创建病人";
    /**
     * 虚拟诊室核心
     */
    //病人已出院或不在治疗中，无法操作
    public static final String PATIENT_CAN_NOT_CURE = "1005";
    public static final String PATIENT_CAN_NOT_CURE_MSG = "病人已出院或不在治疗中，无法操作";

    //病人不属于用户
    public static final String NOT_PATIENT_USER = "1400";
    public static final String NOT_PATIENT_USER_MSG = "只能操作自己的病人哦";

    //用户没有购买病例
    public static final String USER_NOT_BUY_CASE_NEW = "1414";
    public static final String USER_NOT_BUY_CASE_NEW_MSG = "您还没有购买此病例";

    //用户已删除该病例
    public static final String USER_HAS_DELETE_CASE = "1415";
    public static final String USER_HAS_DELETE_CASE_MSG = "该病例已被删除";

    //该病例处在学习中
    public static final String CASE_LEARNING = "1416";
    public static final String CASE_LEARNING_MSG = "该病例处在学习中";

    //用户积分不足
    public static final String USER_MARK_NOT_ENOUGH = "1601";
    public static final String USER_MARK_NOT_ENOUGH_MSG = "您的积分不足";

    //没有更多提示信息
    public static final String NO_MORE_TIPS = "1602";
    public static final String NO_MORE_TIPS_MSG = "当前病程已经没有更多提示信息，不能购买提示";

    //学习中病例数已达上限
    public static final String LEARNING_CASE_OUT_OF_RANGE = "1604";
    public static final String LEARNING_CASE_OUT_OF_RANGE_MSG = "学习中病例数已达上限，请先结束诊疗后再来学习";

    //该病例不是免费病例
    public static final String CASE_NOT_FREE = "1606";
    public static final String CASE_NOT_FREE_MSG = "此病例已取消限时免费，请重新获取限免病例";

    //评估版本过低，暂时无法打开
    public static final String ESTIMATE_NOT_SUPPORT = "1714";
    public static final String ESTIMATE_NOT_SUPPORT_MSG = "评估版本过低，暂时无法打开";

    //诊断不能超过10条
    public static final String ASSERTION_LIMIT = "2001";
    public static final String ASSERTION_LIMIT_MSG = "诊断不能超过10条";

    //候诊室超越间隔1小时
    public static final String WAITING_ROOM_LIMITE_TIME = "19002";
    public static final String WAITING_ROOM_LIMITE_TIME_MSG = "休息1小时，攀杏林高峰";

    //自测存在未出院病人，无法进行超越
    public static final String WAITING_ROOM_EXIT_PATIENT = "19003";
    public static final String WAITING_ROOM_EXIT_PATIENT_MSG = "休息1小时，攀杏林高峰";


    /**
     * 外围
     */
    //用户积分不足
    public static final String USER_MARK_NOT_ENOUGH_OLD = "4042";
    public static final String USER_MARK_NOT_ENOUGH_OLD_MSG = "您的积分不足";

    public static final String CAN_NOT_COMMENT_CASE = "4031";
    public static final String CAN_NOT_COMMENT_CASE_MSG = "用户当前不能对病例进行评论";

    /**
     * 用户中心
     */
    //账号或密码输入不正确
    public static final String ACCOUNT_PWD_ERROR = "3001";
    public static final String ACCOUNT_PWD_ERROR_MSG = "账号或密码输入不正确";

    //注册的邮箱已存在
    public static final String EMAIL_EXIST = "3002";
    public static final String EMAIL_EXIST_MSG = "注册的邮箱已存在";

    //手机已注册
    public static final String PHONE_EXIST = "3003";
    public static final String PHONE_EXIST_MSG = "手机已注册";

    //验证码输入错误
    public static final String VCODE_ERROR = "3004";
    public static final String VCODE_ERROR_MSG = "验证码输入错误";

    //验证码输入错误
    public static final String PHONE_VCODE_ERROR = "3051";
    public static final String PHONE_VCODE_ERROR_MSG = "验证码输入错误";

    //用户名已存在
    public static final String USER_NAME_EXIST = "3005";
    public static final String USER_NAME_EXIST_MSG = "用户名已存在";

    //未登录无法获取数据
    public static final String USER_NOT_LOGIN = "3006";
    public static final String USER_NOT_LOGIN_MSG = "未登录无法获取数据";

    //该邮箱未注册不能用于找回密码
    public static final String EMAIL_NOT_REGISTER = "3016";
    public static final String EMAIL_NOT_REGISTER_MSG = "该邮箱未注册不能用于找回密码";

    //该账号不存在，请先注册
    public static final String ACCOUNT_NOT_EXIST = "3017";
    public static final String ACCOUNT_NOT_EXIST_MSG = "该账号不存在，请先注册";

    //旧密码错误
    public static final String OLD_PASS_ERROR = "3021";
    public static final String OLD_PASS_ERROR_MSG = "旧密码错误";

    //修改密码-验证码错误
    public static final String CHANGE_PASSWORD_CAPTCHA_ERROR = "3024";
    public static final String CHANGE_PASSWORD_CAPTCHA_ERROR_MSG = "验证码错误";

    //修改密码-用户不存在
    public static final String CHANGE_PASSWORD_NO_USER = "3025";
    public static final String CHANGE_PASSWORD_NO_USER_MSG = "用户不存在";

    //修改密码-发送授权码失败
    public static final String CHANGE_PASSWORD_SEND_CAPTCHA_FAIL = "3026";
    public static final String CHANGE_PASSWORD_SEND_CAPTCHA_FAIL_MSG = "发送授权码失败";

    //修改密码-验证授权码失败
    public static final String CHANGE_PASSWORD_CHECK_CAPTCHA_FAIL = "3027";
    public static final String CHANGE_PASSWORD_CHECK_CAPTCHA_FAIL_MSG = "验证授权码失败";

    //修改密码-失败
    public static final String CHANGE_PASSWORD_FAIL = "3028";
    public static final String CHANGE_PASSWORD_FAIL_MSG = "失败";

    //注册-非手机号
    public static final String REGISTER_NOT_PHONE_NUM = "3029";
    public static final String REGISTER_NOT_PHONE_NUM_MSG = "非手机号";

    //发送短信60秒内
    public static final String SEND_SHORT_MSG_IN_SIXTY = "3030";
    public static final String SEND_SHORT_MSG_IN_SIXTY_MSG = "发送短信60秒内";

    //发送短信60秒内
    public static final String SEND_EMAIL_FAIL = "3032";
    public static final String SEND_EMAIL_FAIL_MSG = "邮件发送失败";

    //登录失败
    public static final String LOGIN_FAIL = "3034";
    public static final String LOGIN_FAIL_MSG = "登录失败";

    //用户未登录
    public static final String USER_BIND_PHONE = "3043";
    public static final String USER_BIND_PHONE_MSG = "该手机已经绑定过用户，不允许快速登录";

    //提交的认证信息已存在
    public static final String IDENTIFY_INFO_EXIST = "10108";
    public static final String IDENTIFY_INFO_EXIST_MSG = "您好,您提交的认证信息已存在,请核对您的信息后重新申请认证,如有疑问请联系教办处理。";

    //认证输入信息有误
    public static final String IDENTIFY_INPUT_INFO_FAIL = "10113";
    public static final String IDENTIFY_INPUT_INFO_FAIL_MSG = "您好，请核对您的信息后重新申请认证，如确认无误，请联系机构管理员处理。";

    //已经是审核中状态，请等着审核
    public static final String HAVE_IDENTIFYING_STATE_WAIT = "10114";
    public static final String HAVE_IDENTIFYING_STATE_WAIT_MSG = "已经是审核中状态，请等着审核";

    //机构用户已满员
    public static final String ORGANIZATION_USER_HAD_FILL = "10115";
    public static final String ORGANIZATION_USER_HAD_FILL_MSG = "机构用户已满员";

    //姓名和学号输入有误
    public static final String NAME_AND_STUDY_NUMBER_ERROR = "10116";
    public static final String NAME_AND_STUDY_NUMBER_ERROR_MSG = "认证失败。请再次审核您输入的信息，若确认无误，则请联系您所在机构的管理员，确认其是否上传了您的信息。";

    //姓名输入有误
    public static final String NAME_INPUT_ERROR = "10117";
    public static final String NAME_INPUT_ERROR_MSG = "认证失败。您的姓名与您在所在机构提供的信息不一致，请修改后再试，或与机构的管理员联系确认相关信息是否一致。";

    //学号输入有误
    public static final String STUDY_NUMBER_INPUT_ERROR = "10118";
    public static final String STUDY_NUMBER_INPUT_ERROR_MSG = "认证失败。您的学号与您在所在机构提供的信息不一致，请修改后再试，或与机构的管理员联系确认相关信息是否一致。";


    /**
     * 机构
     */

    //考试不存在
    public static final String EXAM_NOT_EXIST = "10401";
    public static final String EXAM_NOT_EXIST_MSG = "考试不存在";

    //考试报名已截止
    public static final String EXAM_END_FOR_ENTER = "10404";
    public static final String EXAM_END_FOR_ENTER_MSG = "考试报名已截止";

    //考试报名已截止无法取消
    public static final String EXAM_CAN_NOT_CANCEL = "10406";
    public static final String EXAM_CAN_NOT_CANCEL_MSG = "考试报名已截止无法取消";

    //考试时间未到无法开始考试
    public static final String EXAM_NOT_BEGIN = "10409";
    public static final String EXAM_NOT_BEGIN_MSG = "考试时间未到无法开始考试";

    //已报名无法再次报名
    public static final String EXAM_ALREADY_ENTERED = "10413";
    public static final String EXAM_ALREADY_ENTERED_MSG = "本场考试已参与";

    //考试已取消或者删除，不能开始考试
    public static final String EXAM_CAN_NOT_BEGIN_BECAUSE_CANCEL_OR_DELETE = "10416";
    public static final String EXAM_CAN_NOT_BEGIN_BECAUSE_CANCEL_OR_DELETE_MSG = "考试已取消或者删除，不能开始考试";

    //该口令的考试不存在
    public static final String EXAM_CODE_NOT_EXIST = "10431";
    public static final String EXAM_CODE_NOT_EXIST_MSG = "该口令的考试不存在";

    //非该机构人员不能报名该考试
    public static final String EXAM_ONLY_ORGANIZATION_MEMBER_CAN_ENTER = "10432";
    public static final String EXAM_ONLY_ORGANIZATION_MEMBER_CAN_ENTER_MSG = "非该机构人员不能报名该考试";

    //考试结束前5分钟不能报名
    public static final String EXAM_CAN_NOT_ENTER_IN_LATEST_FIVE = "10433";
    public static final String EXAM_CAN_NOT_ENTER_IN_LATEST_FIVE_MSG = "本场考试即将结束，无法参与";
//
//    //本场考试被设定为仅限手机端参与，请到治趣APP参加
//    public static final String EXAM_MOBILE_ONLY = "10438";
//    public static final String EXAM_MOBILE_ONLY_MSG = "本场考试被设定为仅限手机端参与，请到治趣APP参加";

    //本场考试被设定为仅限电脑端参与，请到治趣官网参加
    public static final String EXAM_PC_ONLY = "10439";
    public static final String EXAM_PC_ONLY_MSG = "本场考试被设定为仅限电脑端参与，请到治趣官网参加";

    //该账号已绑定其它用户
    public static final String ACCOUNT_BIND_OTHER_USER = "3047";
    public static final String ACCOUNT_BIND_OTHER_USER_MSG = "该账号已绑定其它用户";

    //您已经被移出本场考试，无法参加考试
    public static final String EXAM_IS_REMOVED_CAN_NOT_JOIN = "10440";
    public static final String EXAM_IS_REMOVED_CAN_NOT_JOIN_MSG = "您已经被移出本场考试，无法参加考试。";

    //不在考试指定对象范围内。若有疑问可与考试组织方联系
    public static final String EXAM_NOT_IN_RANGE = "170003";
    public static final String EXAM_NOT_IN_RANGE_MSG = "您不在考试指定对象范围内。若有疑问可与考试组织方联系。";

    /**
     * 二维码
     */

    //二维码该兑换码已被使用
    public static final String QCODE_USED_MAX = "19000";
    public static final String QCODE_USED_MAX_MSG = "糟糕，兑换的人数太多了，下次再来吧";

    //二维码该兑换码已被使用
    public static final String QCODE_WAS_USED = "19001";
    public static final String QCODE_WAS_USED_MSG = "二维码已兑换";


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


    //考试时间已过无法开始考试
    public static final String EXAM_TIME_OVER_CAN_NOT_DO = "10408";
    public static final String EXAM_TIME_OVER_CAN_NOT_DO_MSG = "考试时间已过无法开始考试";

    //实名认证10115
    public static final String REAL_NAME_FULL = "10115";
    public static final String REAL_NAME_FULL_MSG = "认证人员名额已满";

    //登陆账号不存在
    public static final String LOGIN_ACCOUNT_NOT_EXIT = "129003";
    public static final String LOGIN_ACCOUNT_NOT_EXIT_MSG = "该邮箱还未注册";

    //手机验证码错误
    public static final String PHONE_AUTH_CODE_ERROR = "130001";
    public static final String PHONE_AUTH_CODE_ERROR_MSG = "验证码错误";

    //手机号或密码输入不正确
    public static final String LOGIN_ACCOUNT_ERROR = "129005";
    public static final String LOGIN_ACCOUNT_ERROR_MSG = "手机号或密码输入不正确";

    //手机号已存在
    public static final String PHONE_HAD_EXIT_ERROR = "129001";
    public static final String PHONE_HAD_EXIT_ERROR_MSG = "手机号已存在";

    //用户已绑定手机
    public static final String USER_HAD_BIND_ERROR = "129002";
    public static final String USER_HAD_BIND_ERROR_MSG = "用户已绑定手机";

    //该手机号码还未注册
    public static final String THE_PHONE_NO_REGIST_ERROR = "129004";
    public static final String THE_PHONE_NO_REGIST_ERROR_MSG = "该手机号码还未注册";

    //手机号不存在
    public static final String PHONE_NUMBER_NOT_EXIST_ERROR = "129006";
    public static final String PHONE_NUMBER_NOT_EXIST_ERROR_MSG = "手机号不存在";

    //旧密码错误
    public static final String MODIFY_PSW_ORIGINAL_ERROR = "129010";
    public static final String MODIFY_PSW_ORIGINAL_ERROR_MSG = "旧密码错误";

    //邮箱或密码输入不正确
    public static final String LOGIN_ACCOUNT_EMAIL_ERROR = "129009";
    public static final String LOGIN_ACCOUNT_EMAIL_ERROR_MSG = "邮箱或密码输入不正确";

    //您输入的账号格式有误，请重新输入
    public static final String LOGIN_ACCOUNT_FORMAT_ERROR = "129008";
    public static final String LOGIN_ACCOUNT_FORMAT_ERROR_MSG = "您输入的账号格式有误，请重新输入";

    //邮箱已绑定
    public static final String EMAIL_HAD_BIND_ERROR = "129011";
    public static final String EMAIL_HAD_BIND_ERROR_MSG = "邮箱已绑定其他账户";

    //昵称已存在
    public static final String USER_NAME_EXIST_NEW = "123005";
    public static final String USER_NAME_EXIST__NEW_MSG = "昵称已存在";

    //考试病例已完成治疗
    public static final String EXAM_CASE_FINISHED = "1712";
    public static final String EXAM_CASE_FINISHED_MSG = "您选择的病例已经完成诊疗。";

    //您已经选择了您擅长的全部病例，无法选择其他病例考试。
    public static final String EXAM_CASE_CAN_NOT_CHOOSE = "33333";
    public static final String EXAM_CASE_CAN_NOT_CHOOSE_MSG = "您已经选择了您擅长的全部病例，无法选择其他病例考试";

    //注册邀请码错误
    public static final String REGIST_INVITATION_CODE_ERROR = "129012";
    public static final String REGIST_INVITATION_CODE_ERROR_MSG = "邀请码错误";

    //考试解码错误
    public static final String EXAM_UN_LOCK_ERROR = "170005";
    public static final String EXAM_UN_LOCK_ERROR_MSG = "密码输入错误，请重新输入";

    private static Map<String, String> CODE_MAP = new HashMap<>();

    static {
        CODE_MAP.put(TIME_OUT, TIME_OUT_MSG);
        CODE_MAP.put(NO_USER_ID, NO_USER_ID_MSG);
        CODE_MAP.put(GET_USER_INFO_FAIL, GET_USER_INFO_FAIL_MSG);
        CODE_MAP.put(GET_POST_INFO_FAIL, GET_POST_INFO_FAIL_MSG);
        CODE_MAP.put(NO_USER_SESSION_COUNT, NO_USER_SESSION_COUNT_MSG);
        CODE_MAP.put(NEED_UPDATE, NEED_UPDATE_MSG);
        CODE_MAP.put(FUNCTION_ABANDON, FUNCTION_ABANDON_MSG);
        CODE_MAP.put(NOT_PATIENT_USER, NOT_PATIENT_USER_MSG);
        CODE_MAP.put(USER_NOT_BUY_CASE_NEW, USER_NOT_BUY_CASE_NEW_MSG);
        CODE_MAP.put(USER_HAS_DELETE_CASE, USER_HAS_DELETE_CASE_MSG);
        CODE_MAP.put(CASE_LEARNING, CASE_LEARNING_MSG);
        CODE_MAP.put(USER_MARK_NOT_ENOUGH, USER_MARK_NOT_ENOUGH_MSG);
        CODE_MAP.put(NO_MORE_TIPS, NO_MORE_TIPS_MSG);
        CODE_MAP.put(LEARNING_CASE_OUT_OF_RANGE, LEARNING_CASE_OUT_OF_RANGE_MSG);
        CODE_MAP.put(CASE_NOT_FREE, CASE_NOT_FREE_MSG);
        CODE_MAP.put(USER_MARK_NOT_ENOUGH_OLD, USER_MARK_NOT_ENOUGH_OLD_MSG);
        CODE_MAP.put(ACCOUNT_PWD_ERROR, ACCOUNT_PWD_ERROR_MSG);
        CODE_MAP.put(EMAIL_EXIST, EMAIL_EXIST_MSG);
        CODE_MAP.put(PHONE_EXIST, PHONE_EXIST_MSG);
        CODE_MAP.put(VCODE_ERROR, VCODE_ERROR_MSG);
        CODE_MAP.put(USER_NAME_EXIST, USER_NAME_EXIST_MSG);
        CODE_MAP.put(USER_NOT_LOGIN, USER_NOT_LOGIN_MSG);
        CODE_MAP.put(EMAIL_NOT_REGISTER, EMAIL_NOT_REGISTER_MSG);
        CODE_MAP.put(ACCOUNT_NOT_EXIST, ACCOUNT_NOT_EXIST_MSG);
        CODE_MAP.put(OLD_PASS_ERROR, OLD_PASS_ERROR_MSG);
        CODE_MAP.put(CHANGE_PASSWORD_CAPTCHA_ERROR, CHANGE_PASSWORD_CAPTCHA_ERROR_MSG);
        CODE_MAP.put(CHANGE_PASSWORD_NO_USER, CHANGE_PASSWORD_NO_USER_MSG);
        CODE_MAP.put(CHANGE_PASSWORD_SEND_CAPTCHA_FAIL, CHANGE_PASSWORD_SEND_CAPTCHA_FAIL_MSG);
        CODE_MAP.put(CHANGE_PASSWORD_CHECK_CAPTCHA_FAIL, CHANGE_PASSWORD_CHECK_CAPTCHA_FAIL_MSG);
        CODE_MAP.put(CHANGE_PASSWORD_FAIL, CHANGE_PASSWORD_FAIL_MSG);
        CODE_MAP.put(REGISTER_NOT_PHONE_NUM, REGISTER_NOT_PHONE_NUM_MSG);
        CODE_MAP.put(SEND_SHORT_MSG_IN_SIXTY, SEND_SHORT_MSG_IN_SIXTY_MSG);
        CODE_MAP.put(SEND_EMAIL_FAIL, SEND_EMAIL_FAIL_MSG);
        CODE_MAP.put(LOGIN_FAIL, LOGIN_FAIL_MSG);
        CODE_MAP.put(DATA_UPDATE, DATA_UPDATE_MSG);
        CODE_MAP.put(AUTHORITY_DENY, AUTHORITY_DENY_MSG);
        CODE_MAP.put(ESTIMATE_NOT_SUPPORT, ESTIMATE_NOT_SUPPORT_MSG);
        CODE_MAP.put(EXAM_CODE_NOT_EXIST, EXAM_CODE_NOT_EXIST_MSG);
        CODE_MAP.put(EXAM_ONLY_ORGANIZATION_MEMBER_CAN_ENTER, EXAM_ONLY_ORGANIZATION_MEMBER_CAN_ENTER_MSG);
        CODE_MAP.put(PATIENT_CAN_NOT_CURE, PATIENT_CAN_NOT_CURE_MSG);
        CODE_MAP.put(EXAM_NOT_EXIST, EXAM_NOT_EXIST_MSG);
        CODE_MAP.put(EXAM_ALREADY_ENTERED, EXAM_ALREADY_ENTERED_MSG);
        CODE_MAP.put(EXAM_END_FOR_ENTER, EXAM_END_FOR_ENTER_MSG);
        CODE_MAP.put(EXAM_NOT_BEGIN, EXAM_NOT_BEGIN_MSG);
        CODE_MAP.put(EXAM_CAN_NOT_BEGIN_BECAUSE_CANCEL_OR_DELETE, EXAM_CAN_NOT_BEGIN_BECAUSE_CANCEL_OR_DELETE_MSG);
        CODE_MAP.put(EXAM_CAN_NOT_CANCEL, EXAM_CAN_NOT_CANCEL_MSG);
        CODE_MAP.put(EXAM_CAN_NOT_ENTER_IN_LATEST_FIVE, EXAM_CAN_NOT_ENTER_IN_LATEST_FIVE_MSG);
        CODE_MAP.put(ASSERTION_LIMIT, ASSERTION_LIMIT_MSG);
        CODE_MAP.put(IDENTIFY_INPUT_INFO_FAIL, IDENTIFY_INPUT_INFO_FAIL_MSG);
        CODE_MAP.put(IDENTIFY_INFO_EXIST, IDENTIFY_INFO_EXIST_MSG);
        CODE_MAP.put(HAVE_IDENTIFYING_STATE_WAIT, HAVE_IDENTIFYING_STATE_WAIT_MSG);
        CODE_MAP.put(USER_BIND_PHONE, USER_BIND_PHONE_MSG);
//        CODE_MAP.put(EXAM_MOBILE_ONLY, EXAM_MOBILE_ONLY_MSG);
        CODE_MAP.put(EXAM_PC_ONLY, EXAM_PC_ONLY_MSG);
        CODE_MAP.put(ACCOUNT_BIND_OTHER_USER, ACCOUNT_BIND_OTHER_USER_MSG);
        CODE_MAP.put(CAN_NOT_COMMENT_CASE, CAN_NOT_COMMENT_CASE_MSG);

        CODE_MAP.put(ORGANIZATION_USER_HAD_FILL, ORGANIZATION_USER_HAD_FILL_MSG);
        CODE_MAP.put(NAME_AND_STUDY_NUMBER_ERROR, NAME_AND_STUDY_NUMBER_ERROR_MSG);
        CODE_MAP.put(NAME_INPUT_ERROR, NAME_INPUT_ERROR_MSG);
        CODE_MAP.put(STUDY_NUMBER_INPUT_ERROR, STUDY_NUMBER_INPUT_ERROR_MSG);

        CODE_MAP.put(QCODE_WAS_USED, QCODE_WAS_USED_MSG);
        CODE_MAP.put(QCODE_USED_MAX, QCODE_USED_MAX_MSG);

        CODE_MAP.put(RESPONSE_STATUS_TOKENARGSERROR, RESPONSE_STATUS_TOKENARGSERROR_MSG);
        CODE_MAP.put(RESPONSE_STATUS_SIGNERROR, RESPONSE_STATUS_SIGNERROR_MSG);
        CODE_MAP.put(RESPONSE_STATUS_TOKENFORMATERROR, RESPONSE_STATUS_TOKENFORMATERROR_MSG);
        CODE_MAP.put(RESPONSE_STATUS_TOKENWRONG, RESPONSE_STATUS_TOKENWRONG_MSG);

        CODE_MAP.put(EXAM_TIME_OVER_CAN_NOT_DO, EXAM_TIME_OVER_CAN_NOT_DO_MSG);
        CODE_MAP.put(PHONE_VCODE_ERROR, PHONE_VCODE_ERROR_MSG);

        CODE_MAP.put(REAL_NAME_FULL, REAL_NAME_FULL_MSG);

        CODE_MAP.put(WAITING_ROOM_LIMITE_TIME, WAITING_ROOM_LIMITE_TIME_MSG);
        CODE_MAP.put(WAITING_ROOM_EXIT_PATIENT, WAITING_ROOM_EXIT_PATIENT_MSG);

        CODE_MAP.put(LOGIN_ACCOUNT_NOT_EXIT, LOGIN_ACCOUNT_NOT_EXIT_MSG);
        CODE_MAP.put(LOGIN_ACCOUNT_ERROR, LOGIN_ACCOUNT_ERROR_MSG);
        CODE_MAP.put(PHONE_AUTH_CODE_ERROR, PHONE_AUTH_CODE_ERROR_MSG);
        CODE_MAP.put(PHONE_HAD_EXIT_ERROR, PHONE_HAD_EXIT_ERROR_MSG);
        CODE_MAP.put(USER_HAD_BIND_ERROR, USER_HAD_BIND_ERROR_MSG);
        CODE_MAP.put(THE_PHONE_NO_REGIST_ERROR, THE_PHONE_NO_REGIST_ERROR_MSG);
        CODE_MAP.put(PHONE_NUMBER_NOT_EXIST_ERROR, PHONE_NUMBER_NOT_EXIST_ERROR_MSG);
        CODE_MAP.put(MODIFY_PSW_ORIGINAL_ERROR, MODIFY_PSW_ORIGINAL_ERROR_MSG);
        CODE_MAP.put(LOGIN_ACCOUNT_EMAIL_ERROR, LOGIN_ACCOUNT_EMAIL_ERROR_MSG);
        CODE_MAP.put(LOGIN_ACCOUNT_FORMAT_ERROR, LOGIN_ACCOUNT_FORMAT_ERROR_MSG);
        CODE_MAP.put(EMAIL_HAD_BIND_ERROR, EMAIL_HAD_BIND_ERROR_MSG);
        CODE_MAP.put(USER_NAME_EXIST_NEW, USER_NAME_EXIST__NEW_MSG);

        CODE_MAP.put(EXAM_CASE_FINISHED, EXAM_CASE_FINISHED_MSG);
        CODE_MAP.put(EXAM_CASE_CAN_NOT_CHOOSE, EXAM_CASE_CAN_NOT_CHOOSE_MSG);

        CODE_MAP.put(EXAM_IS_REMOVED_CAN_NOT_JOIN, EXAM_IS_REMOVED_CAN_NOT_JOIN_MSG);
        CODE_MAP.put(EXAM_NOT_IN_RANGE, EXAM_NOT_IN_RANGE_MSG);

        CODE_MAP.put(REGIST_INVITATION_CODE_ERROR, REGIST_INVITATION_CODE_ERROR_MSG);

        CODE_MAP.put(EXAM_UN_LOCK_ERROR, EXAM_UN_LOCK_ERROR_MSG);

    }

    public static String getMsg(String requestCode) {
        if (TextUtils.isEmpty(requestCode)) {
            return "";
        } else if (null == CODE_MAP) {
            return "";
        } else if (!CODE_MAP.containsKey(requestCode)) {
            return "";
        }
        return CODE_MAP.get(requestCode);
    }
}
