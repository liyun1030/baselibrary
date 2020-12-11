package com.common.base.network.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * http请求参数
 */
public class RequestParams {

    protected Map<String, String> params;

    public RequestParams() {
        params = new HashMap<>();
    }

    public void add(String name, String value) {
        params.put(name, value);
    }

    @Override
    public String toString() {
        if (params.isEmpty()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        Set<Map.Entry<String, String>> set = params.entrySet();
        for (Map.Entry<String, String> entry : set) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            } else {
                stringBuilder.append("?");
            }
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }

        return stringBuilder.toString();
    }
}
