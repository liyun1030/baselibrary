package com.common.base.network;

import android.content.Context;
import android.text.TextUtils;

import com.common.base.R;
import com.common.base.application.BaseApplication;
import com.common.base.bean.BaseModel;
import com.common.base.network.config.Header;
import com.common.base.network.config.NetworkManager;
import com.common.base.network.config.RequestParams;
import com.common.base.tool.BaseConstant;
import com.common.base.tool.CommUtils;
import com.common.base.tool.JsonUtils;

import okhttp3.Headers;
import okhttp3.MultipartBody;

/**
 * 网络工具类 供api调用
 * Created by Administrator on 2015/9/29.
 */
public class OkHttpUtil {

    private static final String TAG = "NetworkUtils";

    protected ResponseListener listener;

    protected Context context;

    protected BaseConstant baseConstant;

    private NetworkManager.ResultCallback resultCallback;

    public OkHttpUtil(Context context, BaseConstant baseConstant) {
        this.context = context;
        this.baseConstant = baseConstant;
    }

    public Context getContext() {
        return context;
    }

    public void setListener(ResponseListener l) {
        listener = l;
    }

    private boolean checkNetwork(String type) {
        boolean hasNetwork = ((BaseApplication) context.getApplicationContext()).isHasNetwork();
        if (!hasNetwork) {
            CommUtils.showToast(context, R.string.error_network);
            if (null != listener) {
                listener.failure(baseConstant.getFlag(type), null);
            }
        }
        return hasNetwork;
    }
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
    /**
     * post
     *
     * @param model
     */
    public void post(String serverUrl, final String type, Object model) {
        post(serverUrl, type, null, model, null);
    }

    /**
     * post
     *
     * @param model
     */
    public void post(String serverUrl, Object model) {
        String jsonString = JsonUtils.writeEntity2Json(model);
        if (TextUtils.isEmpty(jsonString)) {
            return;
        }
        int randNumber =Integer.MAX_VALUE;
        NetworkManager.post(serverUrl , randNumber, null,null, jsonString, null,context);
    }

    /**
     * post
     *
     * @param serverUrl
     * @param type
     * @param strParams
     * @param model
     * @param header
     */
    public void post(String serverUrl, final String type, String strParams, Object model, Header[] header) {
        String jsonString = JsonUtils.writeEntity2Json(model);
        if (TextUtils.isEmpty(jsonString)) {
            return;
        }
        post(serverUrl, type, strParams, jsonString, header);
    }

    /**
     * post
     *
     * @param serverUrl
     * @param type
     * @param strParams
     * @param modelString
     * @param headers
     */
    public void post(String serverUrl, final String type, String strParams, String modelString, Header[] headers) {
        if (!checkNetwork(type))
            return;
        NetworkManager.post(serverUrl + type + (null == strParams ? "" : strParams), baseConstant.getFlag(type), appendHeader(headers), headers,modelString, getCallback(),context);
    }

    public void postRb(String serverUrl, final String type, RequestParams params, String modelString) {
        if (!checkNetwork(type)) {
            return;
        }
        NetworkManager.post(serverUrl + type + appendParams(params), baseConstant.getFlag(type), appendHeader(null), null,modelString, getCallback(),context);
    }


    /**
     * post
     *
     * @param serverUrl
     * @param type
     * @param strParams
     * @param modelString
     */
    public void post(String serverUrl, final String type, String strParams, String modelString) {
        post(serverUrl, type, strParams, modelString, null);
    }

    /**
     * post
     *
     * @param serverUrl
     * @param type
     * @param params
     * @param headers
     */
    public void post(String serverUrl, final String type, RequestParams params, Header[] headers) {
        if (!checkNetwork(type)) {
            return;
        }
        NetworkManager.post(serverUrl + type + appendParams(params), baseConstant.getFlag(type), appendHeader(headers), headers,null, getCallback(),context);
    }

    public void post(String serverUrl, final String type, RequestParams params) {
        post(serverUrl, type, params, null);
    }

    /**
     * get，请求图片验证码
     */
    public void getByte(String serverUrl, final String type, RequestParams params, final ResponseByteListener responseByteListener) {
        if (!checkNetwork(type))
            return;
        NetworkManager.get(serverUrl + type + appendParams(params), baseConstant.getFlag(type), null,null, new NetworkManager.ResultByteCallback() {
            @Override
            public void onSuccess(int flag, byte[] response) {
                if (null != responseByteListener) {
                    responseByteListener.success(flag, response);
                }
            }

            @Override
            public void onFailure(int flag, Exception e) {
                if (null != responseByteListener) {
                    responseByteListener.failure(flag, ResponseCodeShow.TIME_OUT);
                }
            }
        },context);

    }

    /**
     * get
     *
     * @param serverUrl
     * @param type
     */
    public void get(String serverUrl, final String type) {
        get(serverUrl, type, "", null, null);
    }

    /**
     * get
     *
     * @param serverUrl
     * @param type
     */
    public void get(String serverUrl, final String type, String strParams) {
        get(serverUrl, type, strParams, null, null);
    }

    /**
     * get
     *
     * @param serverUrl
     * @param type
     */
    public void get(String serverUrl, final String type, String strParams, RequestParams reqParams) {
        get(serverUrl, type, strParams, reqParams, null);
    }


    /**
     * get
     *
     * @param serverUrl
     * @param type
     * @param strParams
     * @param reqParams
     * @param headers
     */
    public void get(String serverUrl, final String type, String strParams, RequestParams reqParams, Header[] headers) {
        if (!checkNetwork(type))
            return;
        NetworkManager.get(serverUrl + type + (null == strParams ? "" : strParams) + appendParams(reqParams), baseConstant.getFlag(type), appendHeader(headers),headers, getCallback(),context);
    }

    public interface ResponseListener {
        void success(int flag, String response);

        void failure(int flag, String errorResponse);
    }


    public interface ResponseByteListener {
        void success(int flag, byte[] response);

        void failure(int flag, String errorResponse);
    }

    /**
     * form表单提交
     *
     * @param serverUrl
     * @param type
     * @param strParams
     * @param headers
     * @param multipartBuilder
     */
    public void form(String serverUrl, final String type, String strParams, Header[] headers, MultipartBody.Builder multipartBuilder) {
        if (!checkNetwork(type)) {
            return;
        }
        NetworkManager.form(serverUrl + type + (null == strParams ? "" : strParams), baseConstant.getFlag(type), appendHeader(headers), multipartBuilder, getCallback(),context);
    }

    /**
     * form表单提交
     *
     * @param serverUrl
     * @param type
     * @param strParams
     * @param headers
     * @param multipartBuilder
     */
    public String form(String serverUrl, final String type, String strParams, Header[] headers, MultipartBody.Builder multipartBuilder, boolean issync) {
        if (!checkNetwork(type)) {
            return null;
        }
        return  NetworkManager.form(serverUrl + type + (null == strParams ? "" : strParams), baseConstant.getFlag(type), appendHeader(headers), multipartBuilder, getCallback(),context, issync);
    }

    public void cancelAllRequests() {
        NetworkManager.cancelAll();
    }

    private String appendParams(RequestParams reqParams) {
        if (null == reqParams) {
            return "";
        } else {
            return "/" + reqParams.toString();
        }
    }

    private Headers.Builder appendHeader(Header[] headers) {
        Headers.Builder builder = new Headers.Builder();
        if (null == headers || 0 == headers.length) {
            return null;
        } else {
            for (Header header : headers) {
                builder.add(header.getName(), header.getValue());
            }
            return builder;
        }
    }

    public void setResultCallback(NetworkManager.ResultCallback resultCallback) {
        this.resultCallback = resultCallback;
    }

    private NetworkManager.ResultCallback getCallback() {
        if (null != resultCallback) {
            return resultCallback;
        } else {
            return new NetworkManager.ResultStringCallback() {
                @Override
                public void onSuccess(int flag, String response) {
                    if (null != listener) {
                        if (!TextUtils.isEmpty(response)) {
                            listener.success(flag, response);
                        } else {
                            listener.failure(flag, ResponseCodeShow.TIME_OUT);
                        }
                    }
                }

                @Override
                public void onFailure(int flag, Exception e) {
                    if (null != listener) {
                        listener.failure(flag, ResponseCodeShow.TIME_OUT);
                    }
                }
            };
        }
    }

    //===================同步执行方法==================

    /**
     * get
     *
     * @param serverUrl
     * @param type
     */
    public <T> T syncGet(Class<T> clazz, String serverUrl, final String type) {
        return syncGet(clazz, serverUrl, type, "", null, null);
    }

    /**
     * syncGet
     */
    public <T> T syncGet(Class<T> clazz, String serverUrl, final String type, String strParams) {
        return syncGet(clazz, serverUrl, type, strParams, null);
    }

    /**
     * syncGet
     */
    public <T> T syncGet(Class<T> clazz, String serverUrl, final String type, String strParams, RequestParams reqParams) {
        return syncGet(clazz, serverUrl, type, strParams, reqParams, null);
    }

    /**
     * syncGet
     */
    public <T> T syncGet(Class<T> clazz, String serverUrl, final String type, String strParams, RequestParams reqParams, Header[] headers) {
        return NetworkManager.syncGet(clazz, serverUrl + type + (null == strParams ? "" : strParams) + appendParams(reqParams), baseConstant.getFlag(type), appendHeader(headers));
    }

    public <T> T syncPost(Class<T> clazz, String serverUrl, final String type, Object model) {
        return syncPost(clazz, serverUrl, type, null, model, null);
    }

    public <T> T syncPost(Class<T> clazz, String serverUrl, final String type, String strParams, Object model) {
        return syncPost(clazz, serverUrl, type, strParams, model, null);
    }

    /**
     * syncPost
     *
     * @param serverUrl
     * @param type
     * @param strParams
     * @param model
     * @param header
     */
    public <T> T syncPost(Class<T> clazz, String serverUrl, final String type, String strParams, Object model, Header[] header) {
        String jsonString = JsonUtils.writeEntity2Json(model);
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        return syncPost(clazz, serverUrl, type, strParams, null, jsonString, header);
    }


    /**
     * syncPost
     */
    public <T> T syncPost(Class<T> clazz, String serverUrl, final String type, String strParams, RequestParams reqParams, String modelString, Header[] headers) {
        return NetworkManager.syncPost(clazz, serverUrl + type + (null == strParams ? "" : strParams) + appendParams(reqParams), baseConstant.getFlag(type), modelString, appendHeader(headers));
    }
}
