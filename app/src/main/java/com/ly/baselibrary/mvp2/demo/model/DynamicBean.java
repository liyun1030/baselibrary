package com.ly.baselibrary.mvp2.demo.model;

import java.io.Serializable;
import java.util.List;

/**
 * 动态bean
 */
public class DynamicBean implements Serializable {


    /**
     * msg : 获取动态成功
     * success : true
     * rows : [{"did":"a4aa2354cae14b68896c25344531e55d","uid":"6e4a0934f3704538993ce5ec0a81bc64","name":"zhujiang我","content":"11111111","date":"1557026637002"},{"did":"d2f14952e2304e47ae682ac6b2610edf","uid":"6e4a0934f3704538993ce5ec0a81bc64","name":"zhujiang我","content":"222222","date":"1557026631212"},{"did":"4c3fdd648194484fad7d3356115bf495","uid":"6e4a0934f3704538993ce5ec0a81bc64","name":"zhujiang我","content":"3333333","date":"1557026625851"},{"did":"db918e830827411bafcc34879b5c1ba1","uid":"6e4a0934f3704538993ce5ec0a81bc64","name":"zhujiang我","content":"4444444","date":"1557026618293"},{"did":"ecccfe90ca9d4abe9d3d80e690ff3068","uid":"6e4a0934f3704538993ce5ec0a81bc64","name":"zhujiang我","content":"assdd","date":"1557026350572","urlList":["http://192.168.191.1:8080/wwwroot/ecccfe90ca9d4abe9d3d80e690ff3068_1557026351281.jpg"]},{"did":"8174c0b5af2443d9b661e9871fa042d0","uid":"6e4a0934f3704538993ce5ec0a81bc64","name":"zhujiang我","content":"hudaxian","date":"1557026316189","urlList":["http://192.168.191.1:8080/wwwroot/8174c0b5af2443d9b661e9871fa042d0_1557026316710.jpg"]},{"did":"73998d1095384f96bddbd2fb695982fa","uid":"6e4a0934f3704538993ce5ec0a81bc64","name":"zhujiang我","content":"而我我去","date":"1557026279086"},{"did":"986cb57b1e5d47e78290c6d2dfaf4097","uid":"6e4a0934f3704538993ce5ec0a81bc64","name":"zhujiang我","content":"呵呵哒","date":"1557026272706"},{"did":"6328b89f405f45bba5f4cb14f5d497bd","uid":"6e4a0934f3704538993ce5ec0a81bc64","name":"zhujiang我","content":"嗯嗯哒","date":"1557026266210"},{"did":"ad9bdb41e04e4e94b8544a498c6100d4","uid":"6e4a0934f3704538993ce5ec0a81bc64","name":"zhujiang我","content":"1111111号GPRS","date":"1557025860009","urlList":["http://192.168.191.1:8080/wwwroot/ad9bdb41e04e4e94b8544a498c6100d4_1557025860318.jpg","http://192.168.191.1:8080/wwwroot/ad9bdb41e04e4e94b8544a498c6100d4_1557025860667.jpg","http://192.168.191.1:8080/wwwroot/ad9bdb41e04e4e94b8544a498c6100d4_1557025860884.jpg"]}]
     */

    private String msg;
    private boolean success;
    private List<RowsBean> rows;

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

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean implements Serializable {
        /**
         * did : a4aa2354cae14b68896c25344531e55d
         * uid : 6e4a0934f3704538993ce5ec0a81bc64
         * name : zhujiang我
         * content : 11111111
         * date : 1557026637002
         * urlList : ["http://192.168.191.1:8080/wwwroot/ecccfe90ca9d4abe9d3d80e690ff3068_1557026351281.jpg"]
         */

        private String did;
        private String uid;
        private String name;
        private String content;
        private String date;
        private List<String> urlList;

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<String> getUrlList() {
            return urlList;
        }

        public void setUrlList(List<String> urlList) {
            this.urlList = urlList;
        }

        @Override
        public String toString() {
            return "RowsBean{" +
                    "did='" + did + '\'' +
                    ", uid='" + uid + '\'' +
                    ", name='" + name + '\'' +
                    ", content='" + content + '\'' +
                    ", date='" + date + '\'' +
                    ", urlList=" + urlList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DynamicBean{" +
                "msg='" + msg + '\'' +
                ", success=" + success +
                ", rows=" + rows +
                '}';
    }
}
