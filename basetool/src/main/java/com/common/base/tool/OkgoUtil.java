package com.common.base.tool;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.view.View;

import com.common.base.bean.BaseBean;
import com.common.base.callback.DialogCallback;
import com.common.base.callback.StringDialogCallback;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.db.CacheManager;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.sina.weibo.sdk.utils.LogUtil;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.HttpUrl;


/**
 * created by 李云 on 2018/12/27
 * 本类的作用:okgo网络请求工具
 */
public class OkgoUtil {
    public interface ResponseListener {
        void success(String response);

        void failure(String errorResponse);
    }
    /**
     * get请求-无参，无header
     */
    public static void get(Context ctx, String url, ResponseListener listener) {
        getWithDialog(ctx, url, null, null, listener);
    }

    /**
     * get请求-有参，无header
     */
    public static void get(Context ctx, String url, Map params, ResponseListener listener) {
        getWithDialog(ctx, url, params, null, listener);
    }

    /**
     * get请求--有对话框
     */
    public static void getWithDialog(final Context ctx, String url, Map params, HttpHeaders headers, final ResponseListener listener) {
        GetRequest<BaseBean> getRequest = OkGo.get(url);
        getRequest.tag(url);
        if (headers != null) {
            getRequest.headers(headers);
        }
        if (params != null) {
            getRequest.params(params);
        }
        getRequest.execute(new DialogCallback<BaseBean>(ctx) {
            @Override
            public void onSuccess(Response<BaseBean> response) {
                if (ctx!=null){
                    Activity activity = CommUtils.getActivityFromView(ctx);
                    if ((activity != null && activity.isFinishing()) || activity == null) return;
                }
                BaseBean result = response.body();
                if (result != null &&result.code!=null&& result.code.equals("2000")) {
                    if (listener != null) {
                        listener.success(JsonUtils.writeEntity2Json(result));
                    }
                } else {
                    if (listener != null) {
                        listener.failure(result.message);
                    }
                }
            }

            @Override
            public void onError(Response<BaseBean> response) {
                if (ctx!=null){
                    Activity activity = CommUtils.getActivityFromView(ctx);
                    if ((activity != null && activity.isFinishing()) || activity == null) return;
                }
                if (listener != null) {
                    Throwable exception = response.getException();
                    String message = exception.getMessage();
                    String localizedMessage = exception.getLocalizedMessage();
                    listener.failure(response.getException().getMessage());
                }
            }
        });
    }

    /**
     * get请求--无对话框
     */
    public static void getNoDialog(Context ctx, String url, Map params, HttpHeaders headers, final ResponseListener listener) {
        GetRequest getRequest = OkGo.get(url);
        getRequest.tag(url);
        if (headers != null) {
            getRequest.headers(headers);
        }
        if (params != null) {
            getRequest.params(params);
        }
        getRequest.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }

    /**
     * head请求
     */
    public static void head(Context ctx, String url, final ResponseListener listener) {
        OkGo.<String>head(url)
                .tag(url)
                .execute(new StringDialogCallback((Activity) ctx) {
                    @Override
                    public void onSuccess(Response<String> response) {
                    }

                    @Override
                    public void onError(Response<String> response) {
                    }
                });
    }

    /**
     * get请求-无参，无header
     */
    public static void post(Context ctx, String url, ResponseListener listener) {
        post(ctx, url, null, listener);
    }

    /**
     * get请求-有参，无header
     */
    public static void post(Context ctx, String url, Map params, ResponseListener listener) {
        post(ctx, url, params, null, listener);
    }

    /**
     * post请求
     */
    public static void post(final Context ctx, String url, Map params, HttpHeaders headers, final ResponseListener listener) {
        PostRequest<BaseBean> postRequest = OkGo.<BaseBean>post(url);
        postRequest.tag(url);
        if (headers != null) {
            postRequest.headers(headers);
        }
//        if (params != null) {
//            postRequest.params(params);
//        }
//        JSONObject json = new JSONObject(params);
        postRequest.params(params);
//        postRequest.upJson(json);
//        postRequest.isMultipart(false);
//        postRequest.params("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        postRequest.execute(new DialogCallback<BaseBean>(ctx) {
            @Override
            public void onSuccess(Response<BaseBean> response) {
                if (ctx!=null){
                    Activity activity = CommUtils.getActivityFromView(ctx);
                    if ((activity != null && activity.isFinishing()) || activity == null) return;
                }
                BaseBean result = response.body();
                //网络正常状态下的请求
                if (result != null && result.code!=null&&result.code.equals("2000")) {
                    if (listener != null) {
                        listener.success(JsonUtils.writeEntity2Json(result));
                    }
                } else {
                    if (listener != null) {
                        listener.failure(result.message);
                    }
                }
            }

            @Override
            public void onError(Response<BaseBean> response) {
                if (ctx!=null){
                    Activity activity = CommUtils.getActivityFromView(ctx);
                    if ((activity != null && activity.isFinishing()) || activity == null) return;
                }
                //网络不正常的结果
                if (listener != null) {
                    listener.failure(response.getException().getMessage());
                }
            }
        });
    }

    /**
     * 上传json示例，字符串，字节，文件等
     */
    public void upJson(Context ctx, String url, final ResponseListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "这里是需要提交的json格式数据");
        params.put("key3", "也可以使用三方工具将对象转成json字符串");
        params.put("key4", "其实你怎么高兴怎么写都行");
        JSONObject jsonObject = new JSONObject(params);

        OkGo.<BaseBean>post(url)
                .tag(url)
                .headers("header1", "headerValue1")
                .upJson(jsonObject)//上传json串
//                .upFile(new File(filePath))//上传文件
                //.isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                //.params("param1", "paramValue1")        // 这里可以上传参数
                //.params("file1", new File("filepath1"))   // 可以添加文件上传
                //.params("file2", new File("filepath2"))     // 支持多文件同时添加上传
                //.addFileParams(keyName, files)    // 这里支持一个key传多个文件
                .execute(new DialogCallback<BaseBean>(ctx) {
                    @Override
                    public void onSuccess(Response<BaseBean> response) {
                        BaseBean result = response.body();
                        if (result != null &&result.code!=null&& result.code.equals("2000")) {
                            if (listener != null) {
                                listener.success(JsonUtils.writeEntity2Json(result));
                            }
                        } else {
                            if (listener != null) {
                                listener.failure(result.message);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseBean> response) {
                        if (listener != null) {
                            listener.failure(response.getException().getMessage());
                        }
                    }
                });
    }

    /**
     * 读取所有的缓存内容
     */
    public void getAllCache(Context ctx) {
        // 获取所有的缓存，但是一般每个缓存的泛型都不一样，所以缓存的泛型使用 ？
        List<CacheEntity<?>> all = CacheManager.getInstance().getAll();
        StringBuilder sb = new StringBuilder();
        sb.append("共" + all.size() + "条缓存：").append("\n\n");
        for (int i = 0; i < all.size(); i++) {
            CacheEntity<?> cacheEntity = all.get(i);
            sb.append("第" + (i + 1) + "条缓存：").append("\n").append(cacheEntity).append("\n\n");
        }

        OkGo.<BaseBean>get("")
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .cacheKey("no_cache")   //对于无缓存模式,该参数无效
                .cacheMode(CacheMode.DEFAULT)//默认
                .cacheKey("cache_default")//默认
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)//请求失败读取缓存
                .cacheKey("request_failed_read_cache")//请求失败读取缓存
                .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)//无缓存则请求网络
                .cacheKey("if_none_cache_request")//无缓存则请求网络
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)//首次读缓存然后请求网络
                .cacheKey("first_cache_then_request")//首次读缓存然后请求网络
                .cacheTime(5000)        //对于无缓存模式,该时间无效
//                .headers("header1", "headerValue1")//
//                .params("param1", "paramValue1")//
                .execute(new DialogCallback(ctx) {
                    @Override
                    public void onSuccess(Response response) {

                    }
                });
    }

    /**
     * cookie操作
     */
    public void cookieOperate() {
        //一般手动取出cookie的目的只是交给 webview 等等，非必要情况不要自己操作
        //获取所有cookie
        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        List<Cookie> allCookie = cookieStore.getAllCookie();
        //获取指定地址的cookie
        HttpUrl httpUrl = HttpUrl.parse("");
        List<Cookie> cookies = cookieStore.getCookie(httpUrl);
        //添加cookie
        Cookie.Builder builder = new Cookie.Builder();
        Cookie cookie = builder.name("myCookieKey1").value("myCookieValue1").domain(httpUrl.host()).build();
        cookieStore.saveCookie(httpUrl, cookie);
        //删除cookie
        cookieStore.removeCookie(httpUrl);
    }

    /**
     * 同步操作
     */
    public void sync(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    //同步会阻塞主线程，必须开线程
//                    Response response = OkGo.get(Urls.URL_JSONOBJECT)//
//                            .tag(this)//
//                            .headers("header1", "headerValue1")//
//                            .params("param1", "paramValue1")//
//                            .execute();  //不传callback即为同步请求

                    Call<String> call = OkGo.<String>get("")//
                            .tag(this)//
                            .headers("header1", "headerValue1")//
                            .params("param1", "paramValue1")//
                            .converter(new StringConvert())//
                            .adapt();
                    Response<String> response = call.execute();

                    Message message = Message.obtain();
                    message.obj = response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
//    /**
//     * 获取课程列表
//     */
//    public void getCourseList(Context ctx, CourseRequestModel reqModel, CourseListener listener) {
//        Map maps = null;
//        if (reqModel != null) {
//            maps = MapUtils.objectToMap(reqModel);
//        }
//        GetRequest<BaseBean<CourseListModel>> getRequest = OkGo.get(Constant.HOST_URL + BaseApiUrl.COURSE_GETLIST);
//        getRequest.tag(ctx);
//        if (maps != null) {
//            getRequest.params(maps);
//        }
//        getRequest.execute(new DialogCallback<BaseBean<CourseListModel>>(ctx) {
//            @Override
//            public void onSuccess(Response<BaseBean<CourseListModel>> response) {
//                BaseBean result = response.body();
//                CourseListModel data = (CourseListModel) result.data;
//            }
//
//            @Override
//            public void onError(Response<BaseBean<CourseListModel>> response) {
//                super.onError(response);
//            }
//        });
//    }

    /**
     * 取消网络请求
     */
    public void cancelRequest(String tag) {
        OkGo.getInstance().cancelTag(tag);
    }

    /**
     * 下载文件到sd卡
     */
    public static void downloadFile(final Context context, String fileUrl, String destFileDir) {
        File tempFile = new File(fileUrl.trim());
        final String fileName = tempFile.getName();
        InputStream input = null;
        FileOutputStream fos = null;

        boolean result = false;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            int length = connection.getContentLength();
            File apkFile = new File(destFileDir, fileName);
            input = new BufferedInputStream(connection.getInputStream());
            fos = new FileOutputStream(apkFile);

            int downloadCount = 0;

            int count;
            byte buf[] = new byte[1024];
            int total = 0;
            while ((count = input.read(buf)) != -1) {
                total += count;
                int progress = total * 100 / length;
                if ((downloadCount == 0) || progress - 2 > downloadCount) {
                    downloadCount += 2;
                }
                fos.write(buf, 0, count);
            }
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CommUtils.showToast(context, fileName + "下载成功!");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (null != fos) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static String getSize(long size){
        if(size>=1024*1024*1024){
            return new Long(size/1073741824L)+"G";
        }else if(size>=1024*1024){
            return new Long(size/1048576L)+"M";
        }else if(size>=1024){
            return new Long(size/1024)+"K";
        }else
            return size+"B";
    }
    /**
     * 上传文件--不带参数
     */
    public static void uploadFile(File file, String url, final ResponseListener listener) {
        if(file!=null) {
            long fileSize= file.length();
            if(fileSize>5*1024*1024){
                if (listener != null) {
                    listener.failure("文件大小不能超过5M!");
                }
                return;
            }
            OkGo.<String>post(url)
                    .params("file", file)
                    .isMultipart(true)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            LogUtil.e("上传成功", response.body());
                            String result = response.body();
                            //网络正常状态下的请求
                            BaseBean bean = JsonUtils.readJson2Entity(result, BaseBean.class);
                            if (bean != null &&bean.code!=null&& bean.code.equals("2000")) {
                                if (listener != null) {
                                    listener.success(result);
                                }
                            } else {
                                if (listener != null) {
                                    listener.failure(bean.message);
                                }
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            if (listener != null) {
                                listener.failure(response.body());
                            }
                        }
                    });
        }
    }

    /**
     * 上传文件--带参数
     */
    public static void uploadFile(File file, String url, Map params, final ResponseListener listener) {
        //上传多个文件
        JSONObject json = new JSONObject(params);
        OkGo.<String>post(url)
                .params("file", file)
                .isMultipart(true)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtil.e("上传成功", response.body());
                        String result = response.body();
                        //网络正常状态下的请求
                        BaseBean bean = JsonUtils.readJson2Entity(result, BaseBean.class);
                        if (bean != null &&bean.code!=null&& bean.code.equals("2000")) {
                            if (listener != null) {
                                listener.success(result);
                            }
                        } else {
                            if (listener != null) {
                                listener.failure(bean.message);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        if (listener != null) {
                            listener.failure(response.body());
                        }
                    }
                });
    }




    /**
     * 上传多个文件--不带参数
     */
    public static void uploadFiles(final Context ctx,List<File> files, String url,  final ResponseListener listener) {
        PostRequest<String> post = OkGo.<String>post(url);
        //上传多个文件
        for (int i=0;i<files.size();i++){
            post.params("file"+i,files.get(i));
        }
        post.isMultipart(true).execute(new DialogCallback<String>(ctx) {

            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e("上传成功", response.body());
                String result = response.body();
                //网络正常状态下的请求
                BaseBean bean = JsonUtils.readJson2Entity(result, BaseBean.class);
                if (bean != null &&bean.code!=null&& bean.code.equals("2000")) {
                    if (listener != null) {
                        listener.success(result);
                    }
                } else {
                    if (listener != null) {
                        listener.failure(bean.message);
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                if (listener != null) {
                    listener.failure(response.body());
                }
            }
        });
    }
    /**
     * 获取返回码.
     *
     * @param json json字符串
     * @return 返回码
     */
    public static String getRequestCode(String json) {
        BaseBean baseModel = JsonUtils.readJson2Entity(json, BaseBean.class);
        if (baseModel.getCode()!=null){
            return baseModel.getCode();
        }else if (baseModel.getMessage()!=null){
            return baseModel.getMessage();
        }
        return "";
    }
}

