package com.common.base.network;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.common.base.bean.StudentHomeRoleModel;
import com.common.base.bean.ThirdLoginByResModel;
import com.common.base.bean.UserLoginReModel;
import com.common.base.okgo.OkgoUtil;
import com.common.base.tool.BaseConstant;
import com.common.base.tool.JsonUtils;
import com.common.base.tool.MapUtils;
import com.common.base.tool.OkHttpResultCallback;

import java.util.Map;

/**
 * created by 李云 on 2019/6/20
 * 本类的作用:用户登录、注册、忘记密码相关
 */
public class CommonBusiness {
    private static CommonBusiness okGoUtil = new CommonBusiness();

    private CommonBusiness() {
    }

    public static CommonBusiness getInstance() {
        return okGoUtil;
    }

    public interface UserListener {
        void loginSucc(ThirdLoginByResModel model);

        void getHomeIndex(StudentHomeRoleModel model);

        void fail(String msg);
    }

    /**
     * 用户所有操作
     */
    public void userOperation(Context ctx, UserLoginReModel reqModel, String url, int method, UserListener listener) {
        Map maps = null;
        if (reqModel != null) {
            maps = MapUtils.objectToMap(reqModel);
        }
        if (method == 1) {
            //get请求
            OkgoUtil.get(ctx, url, maps, new OkgoUtil.ResponseListener() {
                @Override
                public void success(String str) {
                    if (listener != null) {
                        StudentHomeRoleModel model = JsonUtils.readJson2Entity(JSON.parseObject(str).get("data").toString(), StudentHomeRoleModel.class);
                        listener.getHomeIndex(model);
                    }
                }

                @Override
                public void failure(String errorResponse) {
                    if (listener != null) {
                        listener.fail(errorResponse);
                    }
                }
            });
        } else if (method == 2) {
            //post
            OkgoUtil.post(ctx, url, maps, new OkgoUtil.ResponseListener() {
                @Override
                public void success(String response) {
                    if (listener != null) {
                        ThirdLoginByResModel model = JsonUtils.readJson2Entity(response, ThirdLoginByResModel.class);
                        listener.loginSucc(model);
                    }
                }

                @Override
                public void failure(String errorResponse) {
                    if (listener != null) {
                        listener.fail(errorResponse);
                    }
                }
            });
        }
    }

    /**
     * 用户所有操作
     */
    public void userOperationHttp(Context ctx, UserLoginReModel reqModel, String url, int method) {
        Map maps = null;
        if (reqModel != null) {
            maps = MapUtils.objectToMap(reqModel);
        }
        OkHttpUtil util=new OkHttpUtil(ctx, new BaseConstant());
        OkHttpUtil.ResponseListener listener= new  OkHttpUtil.ResponseListener(){
            @Override
            public void success(int flag, String response) {

            }

            @Override
            public void failure(int flag, String errorResponse) {

            }
        };
        util.setListener(listener);
        util.setResultCallback(new OkHttpResultCallback(listener));
        if (method == 1) {
            //get请求
            util.get(BaseConstant.LOGIN_LOGIN_BY_ACCOUNT, BaseConstant.LOGIN_LOGIN_BY_ACCOUNT, null);
        } else if (method == 2) {
            //post

            util.post(BaseConstant.LOGIN_LOGIN_BY_ACCOUNT, reqModel);
        }
    }
}