package base.model;

/**
 * 病例基本信息
 * 注：注意区分匿名用户和已登录用户
 * Created by Yan Kai on 2015/10/23 0023.
 */
public class CaseBaseInfoModel {

    public static final int CASE_STATE_NOT_BUY = 0;
    public static final int CASE_STATE_NOT_STUDY = 1;
    public static final int CASE_STATE_STUDYING = 2;
    public static final int CASE_STATE_STUDIED = 3;

    public static final int FREE_CASE_STATE_NOT_STUDY = 0;
    public static final int FREE_CASE_STATE_STUDYING = 1;
    public static final int FREE_CASE_STATE_STUDIED = 2;

    private String case_id;
    private String case_icon;
    private String display_name;
    private String specialty_classify_name;
    private String scene_disc;
    private String main_auth_id;
    private String main_auth_name;
    private String main_auth_icon;
    private String main_auth_org_name;
    private int difficulty;
    private double rate;
    private int comment_user_count;
    private int user_count;
    private int case_state;
    private int case_type;
    private int if_can_comment;
    private String learning_patient_id;
    private int learning_patient_mode_type;
    private int buy_time;
    private int if_buy;
    private int if_free;
    private int free_case_state;
    private String introspection;
    private int if_collection;
    private int if_show;
    private int case_price;
    private int case_only_display;   //仅供显示 0正常  1仅供显示

    private int my_point;  //我的积分
    private int f_org_case;  //是否是机构病历0否1.是
    private int buy_point;   // 购买病历所需积分

    private int case_discount_price;   //折扣价
    private int buy_discount_point;    //折扣积分
    private int point_discount;   //积分折扣【例：八八折 返回值:88 | 八折 返回值：80】
    private int price_discount;   //金额折扣定价

    public int getCase_discount_price() {
        return case_discount_price;
    }

    public void setCase_discount_price(int case_discount_price) {
        this.case_discount_price = case_discount_price;
    }

    public int getBuy_discount_point() {
        return buy_discount_point;
    }

    public void setBuy_discount_point(int buy_discount_point) {
        this.buy_discount_point = buy_discount_point;
    }

    public int getPoint_discount() {
        return point_discount;
    }

    public void setPoint_discount(int point_discount) {
        this.point_discount = point_discount;
    }

    public int getPrice_discount() {
        return price_discount;
    }

    public void setPrice_discount(int price_discount) {
        this.price_discount = price_discount;
    }

    public int getBuy_point() {
        return buy_point;
    }

    public void setBuy_point(int buy_point) {
        this.buy_point = buy_point;
    }

    public int getMy_point() {
        return my_point;
    }

    public void setMy_point(int my_point) {
        this.my_point = my_point;
    }

    public int getF_org_case() {
        return f_org_case;
    }

    public void setF_org_case(int f_org_case) {
        this.f_org_case = f_org_case;
    }

    public int getCase_price() {
        return case_price;
    }

    public void setCase_price(int case_price) {
        this.case_price = case_price;
    }

    public int getCase_only_display() {
        return case_only_display;
    }

    public void setCase_only_display(int case_only_display) {
        this.case_only_display = case_only_display;
    }
    private int if_can_study;

    public int getIf_can_study() {
        return if_can_study;
    }

    public void setIf_can_study(int if_can_study) {
        this.if_can_study = if_can_study;
    }

    public int getIf_show() {
        return if_show;
    }

    public void setIf_show(int if_show) {
        this.if_show = if_show;
    }

    public int getIf_collection() {
        return if_collection;
    }

    public void setIf_collection(int if_collection) {
        this.if_collection = if_collection;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public void setCase_icon(String case_icon) {
        this.case_icon = case_icon;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public void setSpecialty_classify_name(String specialty_classify_name) {
        this.specialty_classify_name = specialty_classify_name;
    }

    public void setScene_disc(String scene_disc) {
        this.scene_disc = scene_disc;
    }

    public void setMain_auth_id(String main_auth_id) {
        this.main_auth_id = main_auth_id;
    }

    public void setMain_auth_name(String main_auth_name) {
        this.main_auth_name = main_auth_name;
    }

    public void setMain_auth_icon(String main_auth_icon) {
        this.main_auth_icon = main_auth_icon;
    }

    public void setMain_auth_org_name(String main_auth_org_name) {
        this.main_auth_org_name = main_auth_org_name;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setComment_user_count(int comment_user_count) {
        this.comment_user_count = comment_user_count;
    }

    public void setUser_count(int user_count) {
        this.user_count = user_count;
    }

    public void setCase_state(int case_state) {
        this.case_state = case_state;
    }

    public void setIf_can_comment(int if_can_comment) {
        this.if_can_comment = if_can_comment;
    }

    public void setLearning_patient_id(String learning_patient_id) {
        this.learning_patient_id = learning_patient_id;
    }

    public void setLearning_patient_mode_type(int learning_patient_mode_type) {
        this.learning_patient_mode_type = learning_patient_mode_type;
    }

    public void setBuy_time(int buy_time) {
        this.buy_time = buy_time;
    }

    public void setIf_buy(int if_buy) {
        this.if_buy = if_buy;
    }

    public void setIf_free(int if_free) {
        this.if_free = if_free;
    }

    public void setFree_case_state(int free_case_state) {
        this.free_case_state = free_case_state;
    }

    public String getCase_id() {
        return case_id;
    }

    public String getCase_icon() {
        return case_icon;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getSpecialty_classify_name() {
        return specialty_classify_name;
    }

    public String getScene_disc() {
        return scene_disc;
    }

    public String getMain_auth_id() {
        return main_auth_id;
    }

    public String getMain_auth_name() {
        return main_auth_name;
    }

    public String getMain_auth_icon() {
        return main_auth_icon;
    }

    public String getMain_auth_org_name() {
        return main_auth_org_name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public double getRate() {
        return rate;
    }

    public int getComment_user_count() {
        return comment_user_count;
    }

    public int getUser_count() {
        return user_count;
    }

    public int getCase_state() {
        return case_state;
    }

    public int getIf_can_comment() {
        return if_can_comment;
    }

    public String getLearning_patient_id() {
        return learning_patient_id;
    }

    public int getLearning_patient_mode_type() {
        return learning_patient_mode_type;
    }

    public int getBuy_time() {
        return buy_time;
    }

    public int getIf_buy() {
        return if_buy;
    }

    public int getIf_free() {
        return if_free;
    }

    public int getFree_case_state() {
        return free_case_state;
    }

    public String getIntrospection() {
        return introspection;
    }

    public void setIntrospection(String introspection) {
        this.introspection = introspection;
    }

    public int getCase_type() {
        return case_type;
    }

    public void setCase_type(int case_type) {
        this.case_type = case_type;
    }
}
