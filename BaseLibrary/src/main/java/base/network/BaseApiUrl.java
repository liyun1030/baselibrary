package base.network;

/**
 * created by 李云 on 2019/6/17
 * 本类的作用:基类url
 */
public interface BaseApiUrl {
    String HOST_URL = Constant.HOST_URL;
    String PIC_HOST = Constant.PIC_HOST;

    //获取首页详情
    String HOME_DETAIL=HOST_URL+"index";

    //获取内容详情
    String COURSE_DETAIL=HOST_URL+"content/getDetail";

    //免费购买内容
    String COURSE_VIDEO_FREE_BUY=HOST_URL+"content/buy";

    //内容收藏
    String COURSE_VIDEO_COLLECTION=HOST_URL+"user/optStar";


    //小节视频播放时间点
    String COURSE_VIDEO_UPLOAD=HOST_URL+"content/videoTime";

    //小节学习完成
    String COURSE_VIDEO_UPLOAD_COMPLATE=HOST_URL+"content/learnComple";



    //获取试卷题目
    String ANSWER_DETAIL=HOST_URL+"paper/getQuestionData";

    //保存答案
    String ANSWER_SAVE=HOST_URL+"paper/saveQuestion";


    //获取答题卡
    String ANSWER_GET_CARD=HOST_URL+"paper/getSheet";

    //重做
    String ANSWER_RESET=HOST_URL+"paper/reset";


    //获取所有分类
    String SEARCH_ALLCATE=HOST_URL+"index/allCate";


    //获取类型列表
    String SEARCH_TYPE=HOST_URL+"content/getList";


    //获取新闻列表
    String NEWS_TYPE=HOST_URL+"article/index";


    //获取新闻内容
    String NEWS_TYPE_DETAIL=HOST_URL+"article/getContent";


    //获取病例token
    String GET_TOKEN="http://www.curefun.com"+"/cfgateway/login/loginForGuest";


    //获取讨论模块的banner
    String TOPIC_BANNER=HOST_URL+"topic/getBannerInfo";



    //记录内容学习时长
    String STUDY_TIME=HOST_URL+"content/learn";

    //记录病例学习完成
    String CASE_COMPLE=HOST_URL+"content/caseComple";



    //获取学习计划
    String GET_STUDY_PLAN=HOST_URL+"studyplan/getStudyPlanData";



    //课程 题目 添加笔记留言提问
    String ADD_NODE=HOST_URL+"note/addNote";

    //题目详情笔记
    String TOPIC_NODE=HOST_URL+"note/questionNoteDetail";

    /**
     * 获取讨论模块-未读消息数
     */
    public static final String GET_UNREAD_MSG = HOST_URL + "msg/getMyUnReadCount/";
    /**
     * 讨论模块-顶部导航
     */
    public static final String GET_NEW_PLATEINFO = HOST_URL + "topic/getNewPlateInfo";
    /**
     * 获取讨论模块-banner
     */
    public static final String GET_DISCUSS_BANNER = HOST_URL + "topic/getBannerInfo";
    /**
     * 获取讨论模块-主题列表
     */
    public static final String GET_DISCUSS_TOPIC_LIST = HOST_URL + "topic/getTopicList";
    /**
     * 获取讨论模块置顶信息
     */
    public static final String GET_UP_TOPIC_INFO = HOST_URL + "topic/getUpTopicInfo";
    /**
     * 获取分类信息
     */
    public static final String GET_TOPIC_LIST_BY_PLATEID = HOST_URL + "topic/getTopicListByPlateId";
    /**
     * 发表评论
     */
    public static final String CREATE_DISCUSS_MODEL = HOST_URL + "topic/createDiscus";
    public static final String GET_CDN_TOKEN = "/cfgateway/upload/token";
    //讨论区活动接口
    public static final String DISCUSSION_TOPIC_PLATE = HOST_URL + "topic/getDefaultPlate";
    public static final String NEW_CREATE_TALK_TOPIC = HOST_URL + "topic/newCreateTalkTopic";

    /**
     * 关注或取消关注内容
     */
    public static final String FOCUS_CONTENT = HOST_URL + "topic/focusContent";
    /**
     * 点赞或反对功能
     */
    public static final String LIKE_CONTENT = HOST_URL + "topic/likeContent";
    /**
     * 新增回复
     */
    public static final String CREATE_REPLY = HOST_URL + "topic/saveReply";
    /**
     * 查看主题详情
     */
    public static final String GET_TOPIC_DETAIL = HOST_URL + "topic/getTopicDetail";
    /**
     * 举报内容
     */
    public static final String REPORT_CONTENT = HOST_URL + "topic/reportContent";
    /**
     * 账号密码登录
     */
    public static final String LOGIN_LOGIN_BY_ACCOUNT = HOST_URL +"user/login";
    /**
     * 账号注册
     */
    public static final String USER_REG = HOST_URL +"user/register";
    /**
     * 注册后补充信息
     */
    public static final String USER_MODIFY = HOST_URL +"user/modify";
    /**
     * 发送验证码
     */
    public static final String USER_SMS = HOST_URL +"user/sendSms";
    /**
     * 获取用户信息
     */
    public static final String USER_MY = HOST_URL +"user/my";
    /**
     * 图片上传
     */
    public static final String UPLOAD=HOST_URL+"Upload/imageUpload";
    /**
     * 版本更新
     */
    public static final String UPDATE_VERSION=HOST_URL+"index/forceUpdate";
    /**
     * 我的学习获取列表
     */
    public static final String STUDY_LIST=HOST_URL+"user/getList";
    /**
     * 我的课程统计数据
     */
    public static final String COURSE_STAT=HOST_URL+"user/courseStat";
    /**
     * 我的收藏列表
     */
    public static final String COURSE_STAR_LIST=HOST_URL+"user/courseStarList";

    /**
     * 创建微信订单
     */
    public static final String CREATE_WX_PAY_ORDER = "/curefunfinance/pay/createWxPayOrder";
    /**
     * 创建支付宝订单
     */
    public static final String CREATE_ALI_PAY_ORDER = "/curefunfinance/pay/createAliPayOrder";
    /**
     * 检测证件号码是否填了
     */
    public static final String GET_ISUPDATE=HOST_URL+"index/getIsUpdateInfo";

    /**
     * 注册协议http://admin.scjc.com/agreement/contract.html
     */
    public static final String REGISTER_PROTOCOL_ADDRESS ="http://admin.scjc.com/agreement/contract.html";

    /**
     * 获取证书列表
     */
    public static final String GET_CERTIFICATE_LIST =HOST_URL+ "/curefun/curefun3/org/getUserCertificateList/";
    /**
     * 获取证书图片
     */
    public static final String GET_CERTIFICATE_INFO = HOST_URL+"/curefun/curefun3/org/getCertificateInfo/";
    /**
     * 保存证书图片	领取证书
     */
    public static final String SAVE_CERTIFICATE_PIC =HOST_URL+ "user/saveCredential";
    /**
     * 领取证书时获取学习计划课程状态
     */
    public static final String RECEIVE_CERTIFICATE =HOST_URL+ "user/getCoursePlanStat";
    /**
     * 获取证书对应课程
     */
    public static final String CERT_CERT_COURSE_LIST =HOST_URL+ "/curefun/curefun3/org/getCertificateCourseList/";

    /**
     * 获取笔记留言提问
     */
    public static final String GET_NOTE_LIST=HOST_URL+"note/getNoteList";
    /**
     * 添加笔记留言提问
     */
    public static final String ADD_NOTE=HOST_URL+"note/addNote";
    /**
     * 记录设备号
     */
    public static final String SAVE_DEVICE_INFO=HOST_URL+"user/saveDeviceInfo";
    /**
     * 提问详情
     * 地址：/note/getAskDetail
     */
    public static final String GET_ASK_DETAIL=HOST_URL+"note/getAskDetail";
    /**
     *设置用户提醒时间
     * 地址：
     */
    public static final String SET_TIME=HOST_URL+"user/setRemindTime";
}
