package com.common.base.tool;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Json工具类
 * Created by Administrator on 2015/9/28.
 */
public class JsonUtils {

    /**
     * 实体转换为Json
     *
     * @param obj
     * @return
     */
    public static String writeEntity2Json(Object obj) {
        try {
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json转换为实体
     *
     * @param json
     * @param object
     * @return
     */
    public static <T> T readJson2Entity(String json, Class<T> object) {
        try {
            return JSON.parseObject(json, object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json转换为List
     *
     * @param json
     * @param object
     * @param <T>
     * @return
     */
    public static <T> List<T> readJson2Array(String json, Class<T> object) {
        try {
            return JSON.parseArray(json, object);
        } catch (Exception e) {
            return null;
        }
    }

}
