package com.common.base.bean;

/**
 * created by 李云 on 2019/9/19
 */
import java.io.Serializable;

public class BaseModel implements Serializable {
    //----------------------------------------------java老接口
    /**
     * 0000表示成功
     */
    protected String message;
    /**
     * success表示成功
     */
    protected String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


    //---------------------------------------------php接口返回的json格式兼容 同时重新处理错误码
    protected String responseType; //共用
    protected String responseCode;  //相当于message
    protected String responseMsg; //共同错误提示

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}
