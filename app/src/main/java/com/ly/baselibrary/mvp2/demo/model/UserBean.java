package com.ly.baselibrary.mvp2.demo.model;

/**
 * 用户bean
 */
public class UserBean {


    /**
     * msg : 查询成功
     * success : true
     * rows : {"uid":"111111111","account":"123456","password":"123456","name":"??"}
     */

    private String msg;
    private boolean success;
    private RowsBean rows;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public RowsBean getRows() {
        return rows;
    }

    public void setRows(RowsBean rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * uid : 111111111
         * account : 123456
         * password : 123456
         * name : 爱你
         */

        private String uid;
        private String account;
        private String password;
        private String name;
        private String photo;

        @Override
        public String toString() {
            return "RowsBean{" +
                    "uid='" + uid + '\'' +
                    ", account='" + account + '\'' +
                    ", password='" + password + '\'' +
                    ", name='" + name + '\'' +
                    ", photo='" + photo + '\'' +
                    '}';
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "msg='" + msg + '\'' +
                ", success=" + success +
                ", rows=" + rows +
                '}';
    }
}
