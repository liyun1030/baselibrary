package com.common.base.tool;

import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.db.UploadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.task.XExecutor;
import com.lzy.okserver.upload.UploadListener;
import com.lzy.okserver.upload.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * created by 李云 on 2018/12/28
 * 本类的作用:okgo-upload
 */
public class OkUploadUtil implements XExecutor.OnAllTaskEndListener {
    private OkUpload okUpload;
    private List<UploadTask<?>> tasks;//上传任务
    private int type = -1;
    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;
    List<ImageItem> images;

    public OkUploadUtil() {
        okUpload = OkUpload.getInstance();
        okUpload.getThreadPool().setCorePoolSize(1);
        okUpload.addOnAllTaskEndListener(this);
        upload();
    }

    /**
     * 模拟上传数据
     */
    public void upload() {
        images = new ArrayList<>();//上传的图片对象
        List<UploadTask<?>> values = new ArrayList<>();
        if (images != null) {
            Random random = new Random();
            for (int i = 0; i < images.size(); i++) {
                ImageItem imageItem = images.get(i);
                //这里是演示可以传递任何数据
                PostRequest<String> postRequest = OkGo.<String>post("uploadUrl")//
                        .headers("aaa", "111")//
                        .params("bbb", "222")//
                        .params("fileKey" + i, new File(imageItem.path))//
                        .converter(new StringConvert());

                UploadTask<String> task = OkUpload.request(imageItem.path, postRequest)//
                        .priority(random.nextInt(100))//
                        .extra1(imageItem)//
                        .save();
                //注册监听
                String tag = createTag(task);
                task.register(new ListUploadListener(tag))//上传监听
                        .register(new LogUploadListener<String>());//上传日志监听
                values.add(task);
            }
        }
        tasks = values;
        //开始上传
        for (UploadTask<?> task : tasks) {
            task.start();
        }
    }

    private String createTag(UploadTask task) {
        return type + "_" + task.progress.tag;
    }

    private class ListUploadListener extends UploadListener<String> {
        ListUploadListener(Object tag) {
            super(tag);
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(String s, Progress progress) {
            if (type != -1) updateData(type);
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }

    public List<UploadTask<?>> updateData(List<ImageItem> images) {
        this.type = -1;
        this.images = images;
        if (images != null) {
            Random random = new Random();
            for (int i = 0; i < images.size(); i++) {
                ImageItem imageItem = images.get(i);
                //这里是演示可以传递任何数据
                PostRequest<String> postRequest = OkGo.<String>post("")//
                        .headers("aaa", "111")//
                        .params("bbb", "222")//
                        .params("fileKey" + i, new File(imageItem.path))//
                        .converter(new StringConvert());

                UploadTask<String> task = OkUpload.request(imageItem.path, postRequest)//
                        .priority(random.nextInt(100))//
                        .extra1(imageItem)//
                        .save();
                tasks.add(task);
            }
        }
        return tasks;
    }

    public void updateData(int type) {
        //这里是将数据库的数据恢复
        this.type = type;
        if (type == TYPE_ALL) tasks = OkUpload.restore(UploadManager.getInstance().getAll());
        if (type == TYPE_FINISH)
            tasks = OkUpload.restore(UploadManager.getInstance().getFinished());
        if (type == TYPE_ING) tasks = OkUpload.restore(UploadManager.getInstance().getUploading());

        //由于Converter是无法保存下来的，所以这里恢复任务的时候，需要额外传入Converter，否则就没法解析数据
        //至于数据类型，统一就行，不一定非要是String
        for (UploadTask<?> task : tasks) {
            //noinspection unchecked
            Request<String, ? extends Request> request = (Request<String, ? extends Request>) task.progress.request;
            request.converter(new StringConvert());
        }
    }

    /**
     * 上传任务操作：开始，停止，删除
     *
     * @param task
     */
    public void operate(UploadTask<?> task) {
        //删除
        task.remove();
        if (type == -1) {
            int removeIndex = -1;
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).path.equals(task.progress.tag)) {
                    removeIndex = i;
                    break;
                }
            }
            if (removeIndex != -1) {
                images.remove(removeIndex);
            }
            updateData(images);
        } else {
            updateData(type);
        }
        //重新开始
        task.restart();
        //更新
        Progress progress = task.progress;
        switch (progress.status) {
            case Progress.PAUSE:
            case Progress.NONE:
            case Progress.ERROR:
                task.start();
                break;
            case Progress.LOADING:
                task.pause();
                break;
            case Progress.FINISH:
                break;
        }
        //删除所有
        OkUpload.getInstance().removeAll();
        updateData(TYPE_ALL);
    }

    @Override
    public void onAllTaskEnd() {
        //上传结束
    }

    public void onDestroy() {
        okUpload.removeOnAllTaskEndListener(this);
        unRegister();
    }

    public void unRegister() {
        Map<String, UploadTask<?>> taskMap = OkUpload.getInstance().getTaskMap();
        for (UploadTask<?> task : taskMap.values()) {
            task.unRegister(createTag(task));
        }
    }

    public class LogUploadListener<T> extends UploadListener<T> {

        public LogUploadListener() {
            super("LogUploadListener");
        }

        @Override
        public void onStart(Progress progress) {
            System.out.println("onStart: " + progress);
        }

        @Override
        public void onProgress(Progress progress) {
            System.out.println("onProgress: " + progress);
        }

        @Override
        public void onError(Progress progress) {
            System.out.println("onError: " + progress);
            progress.exception.printStackTrace();
        }

        @Override
        public void onFinish(T t, Progress progress) {
            System.out.println("onFinish: " + progress);
        }

        @Override
        public void onRemove(Progress progress) {
            System.out.println("onRemove: " + progress);
        }
    }

}
