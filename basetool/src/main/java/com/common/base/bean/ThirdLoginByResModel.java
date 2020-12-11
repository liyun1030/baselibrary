package com.common.base.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.Item;

public class ThirdLoginByResModel extends BaseModel {

    /**
     * data : {user_info:{user_id:20170220010000101,user_name:秋x逝616101,user_pwd:,user_realname:,user_email:cf09732492@zhiqu.com,user_phone:null,user_avatar:http://tvax2.sinaimg.cn/crop.0.0.996.996.50/6b696979ly8fdu9o7x2ggj20ro0roacm.jpg,office_check:null,developer_check:0,user_birth:null,gender:1,user_sign:null},token:20170220010000101_634680a4fa6d46c1b8517094b8a1c784,need_phone_binding:0}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * user_info : {user_id:20170220010000101,user_name:秋x逝616101,user_pwd:,user_realname:,user_email:cf09732492@zhiqu.com,user_phone:null,user_avatar:http://tvax2.sinaimg.cn/crop.0.0.996.996.50/6b696979ly8fdu9o7x2ggj20ro0roacm.jpg,office_check:null,developer_check:0,user_birth:null,gender:1,user_sign:null}
         * token : 20170220010000101_634680a4fa6d46c1b8517094b8a1c784
         * need_phone_binding : 0
         */
        //---------机构教师学生端合并新添加的字段--------
        private List<OrgsBean> orgs;
        //-----------------

        private UserInfoBean user_info;
        private String token;
        private String open_id;
        private int need_phone_binding;
        private String org_id;
        private String[] h_id;
        private String userid;
        private String username;
        private int org_num;//机构数
        private String selDepartId;//科室id

        public String getSelDepartId() {
            return selDepartId;
        }

        public void setSelDepartId(String selDepartId) {
            this.selDepartId = selDepartId;
        }


        public int getOrg_num() {
            return org_num;
        }

        public void setOrg_num(int org_num) {
            this.org_num = org_num;
        }

        public OrgInfoBean getOrg_info() {
            return org_info;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setOrg_info(OrgInfoBean org_info) {
            this.org_info = org_info;
        }

        private OrgInfoBean org_info;

        public String getOrg_id() {
            return org_id;
        }

        public void setOrg_id(String org_id) {
            this.org_id = org_id;
        }

        public String[] getH_id() {
            return h_id;
        }

        public void setH_id(String[] h_id) {
            this.h_id = h_id;
        }

        public UserInfoBean getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfoBean user_info) {
            this.user_info = user_info;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getNeed_phone_binding() {
            return need_phone_binding;
        }

        public void setNeed_phone_binding(int need_phone_binding) {
            this.need_phone_binding = need_phone_binding;
        }

        public String getOpen_id() {
            return open_id;
        }

        public void setOpen_id(String open_id) {
            this.open_id = open_id;
        }

        public static class OrgInfoBean {

            /**
             * org_id : 1
             * hospitails : [{h_id:25,h_name:机构医院1}]
             * h_id : [25]
             */
            private String name;
            private String org_id;
            private List<HospitailsBean> hospitails;
            private List<String> h_id;
            private String bg_img;
            private String logo;
            private String list_logo;
            private String hardware_logo;

            public String getBg_img() {
                return bg_img;
            }

            public void setBg_img(String bg_img) {
                this.bg_img = bg_img;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getList_logo() {
                return list_logo;
            }

            public void setList_logo(String list_logo) {
                this.list_logo = list_logo;
            }

            public String getHardware_logo() {
                return hardware_logo;
            }

            public void setHardware_logo(String hardware_logo) {
                this.hardware_logo = hardware_logo;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOrg_id() {
                return org_id;
            }

            public void setOrg_id(String org_id) {
                this.org_id = org_id;
            }

            public List<HospitailsBean> getHospitails() {
                return hospitails;
            }

            public void setHospitails(List<HospitailsBean> hospitails) {
                this.hospitails = hospitails;
            }

            public List<String> getH_id() {
                return h_id;
            }

            public void setH_id(List<String> h_id) {
                this.h_id = h_id;
            }

            public static class HospitailsBean implements Serializable{
                /**
                 * h_id : 25
                 * h_name : 机构医院1
                 */

                private String h_id;
                private String h_name;
                private int h_status;//医院状态 1 正常 2 禁用;
                private boolean check;//选中
                public String getH_id() {
                    return h_id;
                }

                public void setH_id(String h_id) {
                    this.h_id = h_id;
                }

                public String getH_name() {
                    return h_name;
                }

                public void setH_name(String h_name) {
                    this.h_name = h_name;
                }

                public int getH_status() {
                    return h_status;
                }

                public void setH_status(int h_status) {
                    this.h_status = h_status;
                }

                public boolean isCheck() {
                    return check;
                }

                public void setCheck(boolean check) {
                    this.check = check;
                }
            }
        }

        public static class UserInfoBean {
            /**
             * user_id : 20170220010000101
             * user_name : 秋x逝616101
             * user_pwd :
             * user_realname :
             * user_email : cf09732492@zhiqu.com
             * user_phone : null
             * user_avatar : http://tvax2.sinaimg.cn/crop.0.0.996.996.50/6b696979ly8fdu9o7x2ggj20ro0roacm.jpg
             * office_check : null
             * developer_check : 0
             * user_birth : null
             * gender : 1
             * user_sign : null
             */

            private String user_id;
            private String user_name;
            private String user_pwd;
            private String user_realname;
            private String user_email;
            private String user_phone;
            private String user_avatar;
            private int office_check;
            private int developer_check;
            private String user_birth;
            private int gender;
            private String user_sign;
            private String invitation;
            private String password;
            private String user_display_org;
            private String user_icon;
            private int user_status;
            private int user_type;

            public int getUser_type() {
                return user_type;
            }

            public void setUser_type(int user_type) {
                this.user_type = user_type;
            }

            public String getUser_icon() {
                return user_icon;
            }

            public void setUser_icon(String user_icon) {
                this.user_icon = user_icon;
            }

            public int getUser_status() {
                return user_status;
            }

            public void setUser_status(int user_status) {
                this.user_status = user_status;
            }

            public String getUser_display_org() {
                return user_display_org;
            }

            public void setUser_display_org(String user_display_org) {
                this.user_display_org = user_display_org;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getInvitation() {
                return invitation;
            }

            public void setInvitation(String invitation) {
                this.invitation = invitation;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getUser_pwd() {
                return user_pwd;
            }

            public void setUser_pwd(String user_pwd) {
                this.user_pwd = user_pwd;
            }

            public String getUser_realname() {
                return user_realname;
            }

            public void setUser_realname(String user_realname) {
                this.user_realname = user_realname;
            }

            public String getUser_email() {
                return user_email;
            }

            public void setUser_email(String user_email) {
                this.user_email = user_email;
            }

            public String getUser_phone() {
                return user_phone;
            }

            public void setUser_phone(String user_phone) {
                this.user_phone = user_phone;
            }

            public String getUser_avatar() {
                return user_avatar;
            }

            public void setUser_avatar(String user_avatar) {
                this.user_avatar = user_avatar;
            }

            public int getOffice_check() {
                return office_check;
            }

            public void setOffice_check(int office_check) {
                this.office_check = office_check;
            }

            public int getDeveloper_check() {
                return developer_check;
            }

            public void setDeveloper_check(int developer_check) {
                this.developer_check = developer_check;
            }

            public String getUser_birth() {
                return user_birth;
            }

            public void setUser_birth(String user_birth) {
                this.user_birth = user_birth;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getUser_sign() {
                return user_sign;
            }

            public void setUser_sign(String user_sign) {
                this.user_sign = user_sign;
            }
        }

        public List<OrgsBean> getOrgs() {
            return orgs;
        }

        public void setOrgs(List<OrgsBean> orgs) {
            this.orgs = orgs;
        }

        public static class OrgsBean implements Serializable, Item {
            /**
             * ue_type : 2
             * ue_office_check : 1
             * org_id : 20200528030100000
             * ue_h_id : 0
             * ue_birth :
             * ue_gender : 2
             * org_info : {name:智慧医疗大平台,bg_img://resourcetest.curefun.com/20200622/6369FECBB38-086D-4a76-BF72-849E4CC31041.png,logo://resourcetest.curefun.com/20200622/2319FECBB38-086D-4a76-BF72-849E4CC31041.png,list_logo:,hardware_logo:http://resourcetest.curefun.com/20200528/QN_b8bf2bd4786c46d3af2c2323351a935d.png,pay_month_total:-1,pay_date:2020-05-27}
             * expiring : {isExpiring:0,type:1,left_day:0}
             */

            private int ue_type;//1.学员 2.老师
            private int ue_office_check;//机构认证 2.未认证 1.认证
            private String org_id;
            private String[] h_id;
            private String ue_birth;
            private int ue_gender;
            private OrgInfoBean org_info;
            private ExpiringBean expiring;
            private String ue_role;//学生老师角色
            private int ue_user_status;//机构下帐号状态 1为正常,2:禁用
            private boolean isCheck;//0未选中 1选中
            private String cert_type;//证件类型
            private String cert_number;//证件号码
            private ArrayList<DataBean.OrgInfoBean.HospitailsBean> hospitails;
            private String ue_identity;//1 住院医师 2 实习医生 3 在校学生


            public String getCert_type() {
                return cert_type;
            }

            public void setCert_type(String cert_type) {
                this.cert_type = cert_type;
            }

            public String getCert_number() {
                return cert_number;
            }

            public void setCert_number(String cert_number) {
                this.cert_number = cert_number;
            }

            public ArrayList<DataBean.OrgInfoBean.HospitailsBean> getHospitails() {
                return hospitails;
            }

            public String getUe_identity() {
                return ue_identity;
            }

            public void setUe_identity(String ue_identity) {
                this.ue_identity = ue_identity;
            }

            public void setHospitails(ArrayList<DataBean.OrgInfoBean.HospitailsBean> hospitails) {
                this.hospitails = hospitails;
            }
            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public int getUe_user_status() {
                return ue_user_status;
            }

            public void setUe_user_status(int ue_user_status) {
                this.ue_user_status = ue_user_status;
            }

            public String getUe_role() {
                return ue_role;
            }

            public void setUe_role(String ue_role) {
                this.ue_role = ue_role;
            }

            public int getUe_type() {
                return ue_type;
            }

            public void setUe_type(int ue_type) {
                this.ue_type = ue_type;
            }

            public int getUe_office_check() {
                return ue_office_check;
            }

            public void setUe_office_check(int ue_office_check) {
                this.ue_office_check = ue_office_check;
            }

            public String getOrg_id() {
                return org_id;
            }

            public void setOrg_id(String org_id) {
                this.org_id = org_id;
            }

            public String[] getH_id() {
                return h_id;
            }

            public void setH_id(String[] h_id) {
                this.h_id = h_id;
            }

            public String getUe_birth() {
                return ue_birth;
            }

            public void setUe_birth(String ue_birth) {
                this.ue_birth = ue_birth;
            }

            public int getUe_gender() {
                return ue_gender;
            }

            public void setUe_gender(int ue_gender) {
                this.ue_gender = ue_gender;
            }

            public OrgInfoBean getOrg_info() {
                return org_info;
            }

            public void setOrg_info(OrgInfoBean org_info) {
                this.org_info = org_info;
            }

            public ExpiringBean getExpiring() {
                return expiring;
            }

            public void setExpiring(ExpiringBean expiring) {
                this.expiring = expiring;
            }

            public static class OrgInfoBean {
                /**
                 * name : 智慧医疗大平台
                 * bg_img : //resourcetest.curefun.com/20200622/6369FECBB38-086D-4a76-BF72-849E4CC31041.png
                 * logo : //resourcetest.curefun.com/20200622/2319FECBB38-086D-4a76-BF72-849E4CC31041.png
                 * list_logo :
                 * hardware_logo : http://resourcetest.curefun.com/20200528/QN_b8bf2bd4786c46d3af2c2323351a935d.png
                 * pay_month_total : -1
                 * pay_date : 2020-05-27
                 */

                private String name;
                private String bg_img;
                private String logo;
                private String list_logo;
                private String hardware_logo;
                private int pay_month_total;
                private String pay_date;
                private String org_id;
                private boolean select;

                public boolean isSelect() {
                    return select;
                }

                public void setSelect(boolean select) {
                    this.select = select;
                }

                public String getOrg_id() {
                    return org_id;
                }

                public void setOrg_id(String org_id) {
                    this.org_id = org_id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getBg_img() {
                    return bg_img;
                }

                public void setBg_img(String bg_img) {
                    this.bg_img = bg_img;
                }

                public String getLogo() {
                    return logo;
                }

                public void setLogo(String logo) {
                    this.logo = logo;
                }

                public String getList_logo() {
                    return list_logo;
                }

                public void setList_logo(String list_logo) {
                    this.list_logo = list_logo;
                }

                public String getHardware_logo() {
                    return hardware_logo;
                }

                public void setHardware_logo(String hardware_logo) {
                    this.hardware_logo = hardware_logo;
                }

                public int getPay_month_total() {
                    return pay_month_total;
                }

                public void setPay_month_total(int pay_month_total) {
                    this.pay_month_total = pay_month_total;
                }

                public String getPay_date() {
                    return pay_date;
                }

                public void setPay_date(String pay_date) {
                    this.pay_date = pay_date;
                }
            }

            public static class ExpiringBean {
                /**
                 * isExpiring : 0
                 * type : 1
                 * left_day : 0
                 */

                private int isExpiring;//是否过期 0 未过期 1 过期了
                private int type;// 1 永久 2 限时
                private int left_day;//过期天数 type = 2 有效

                public int getIsExpiring() {
                    return isExpiring;
                }

                public void setIsExpiring(int isExpiring) {
                    this.isExpiring = isExpiring;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public int getLeft_day() {
                    return left_day;
                }

                public void setLeft_day(int left_day) {
                    this.left_day = left_day;
                }
            }
        }

    }
}
