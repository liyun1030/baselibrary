package base.model;

import java.util.List;

import me.drakeet.multitype.Item;

/**
 * 订单列表中 item
 */

public class OrderInfoModel extends BaseModel {
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        public List<Info> order_infos;
        public int total;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<Info> getOrder_infos() {
            return order_infos;
        }

        public void setOrder_infos(List<Info> order_infos) {
            this.order_infos = order_infos;
        }
    }

    public static class Info implements Item {
        private String order_id;  //单号
        private String case_type; //病例类型 0-本科 1-住培 10 专家本科 11 专家住陪
        private String case_id;//病例ID
        private String case_name;//病例名称
        private String case_icon;//病例图像
        private String case_complaint; //主诉
        private String author_name; //作者名称
        private String author_title; //作者职位
        private String case_org; //病例所属单位
        private int buy_point; //病例所需积分
        private int free_limit;//订单状态 0未支付 1已支付 2问题订单 3订单过期
        private double case_price;//单价
        private String payment_price;//实际支付
        private int case_discount_price;//折扣价
        private int buy_discount_point;//折扣积分
        private int case_only_display;//仅供显示 0正常  1仅供显示
        //----------------------------------
        private int order_type;////订单类型 1病例2课程3课程组合
        private int study_sum;//学习人数

        public String getPayment_price() {
            return payment_price;
        }

        public void setPayment_price(String payment_price) {
            this.payment_price = payment_price;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getCase_type() {
            return case_type;
        }

        public void setCase_type(String case_type) {
            this.case_type = case_type;
        }

        public String getCase_id() {
            return case_id;
        }

        public void setCase_id(String case_id) {
            this.case_id = case_id;
        }

        public String getCase_name() {
            return case_name;
        }

        public void setCase_name(String case_name) {
            this.case_name = case_name;
        }

        public String getCase_icon() {
            return case_icon;
        }

        public void setCase_icon(String case_icon) {
            this.case_icon = case_icon;
        }

        public String getCase_complaint() {
            return case_complaint;
        }

        public void setCase_complaint(String case_complaint) {
            this.case_complaint = case_complaint;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getAuthor_title() {
            return author_title;
        }

        public void setAuthor_title(String author_title) {
            this.author_title = author_title;
        }

        public String getCase_org() {
            return case_org;
        }

        public void setCase_org(String case_org) {
            this.case_org = case_org;
        }

        public int getBuy_point() {
            return buy_point;
        }

        public void setBuy_point(int buy_point) {
            this.buy_point = buy_point;
        }

        public int getFree_limit() {
            return free_limit;
        }

        public void setFree_limit(int free_limit) {
            this.free_limit = free_limit;
        }

        public double getCase_price() {
            return case_price;
        }

        public void setCase_price(double case_price) {
            this.case_price = case_price;
        }

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

        public int getCase_only_display() {
            return case_only_display;
        }

        public void setCase_only_display(int case_only_display) {
            this.case_only_display = case_only_display;
        }

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }

        public int getStudy_sum() {
            return study_sum;
        }

        public void setStudy_sum(int study_sum) {
            this.study_sum = study_sum;
        }
    }

}
