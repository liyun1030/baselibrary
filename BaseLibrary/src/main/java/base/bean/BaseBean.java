package base.bean;

import java.io.Serializable;

/**
 * 接口请求基类
 */
public class BaseBean<T> implements Serializable {
    public String message;
    public String status;
    public String code;
    public T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
