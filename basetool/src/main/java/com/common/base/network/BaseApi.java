package com.common.base.network;

import com.common.base.bean.BaseModel;
import com.common.base.tool.JsonUtils;

/**
 * api接口基类
 * Created by Yan Kai on 2016/3/14.
 */
public class BaseApi {

    protected OkHttpUtil utils;
    /**
     * 获取返回码.
     *
     * @param json json字符串
     * @return 返回码
     */
    public static String getRequestCode(String json) {
        BaseModel baseModel = JsonUtils.readJson2Entity(json, BaseModel.class);
        if (baseModel.getResponseCode() != null) {
            return baseModel.getResponseCode();
        } else if (baseModel.getMessage() != null) {
            return baseModel.getMessage();
        }
        return "";
//        return null == baseModel ? "" : baseModel.getMessage();
    }


    public void cancelAllRequests() {
        if (null != utils) {
            utils.cancelAllRequests();
        }
    }

    /**
     * 获取返回码 responseType
     *
     * @param json json字符串
     * @return responseType
     */
    public static String getResponseType(String json) {
        BaseModel baseModel = JsonUtils.readJson2Entity(json, BaseModel.class);
        return null == baseModel ? "" : baseModel.getResponseType();
    }


    /**
     * 获取错误提示语句
     *
     * @param json json字符串
     * @return
     */
    public static String getResponseMsg(String json) {
        BaseModel baseModel = JsonUtils.readJson2Entity(json, BaseModel.class);
        if (baseModel != null) {
            if (baseModel.getResponseMsg() != null && !baseModel.getResponseMsg().equals("")) {
                return baseModel.getResponseMsg();
            }else if (baseModel.getMessage() != null && !baseModel.getMessage().equals("")) {
                return ResponseCodeShow.getMsg(baseModel.getMessage());
            }
        } else {
            return "未知错误";
        }
        return "未知错误";
    }

}
