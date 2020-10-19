package com.common.base.arouter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 *ARouter路由跳转工具类
 */
public class ARouterManager {


    public static  void startToVideo(String c_id){
//        // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
//        ARouter.getInstance().build("/test/activity").navigation();
        // 2. 跳转并携带参数
        ARouter.getInstance().build(ARouterPathConfig.VIDEO_START)
//                .withLong("key1", 666L)
                .withString("c_id", c_id)
//                .withObject("key4", new Test("Jack", "Rose"))
                .navigation();
    }



    public static  void startToTopic(String id){
//        // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
//        ARouter.getInstance().build("/test/activity").navigation();
        // 2. 跳转并携带参数
        ARouter.getInstance().build(ARouterPathConfig.TOPIC_START)
//                .withLong("key1", 666L)
                .withString("c_id", id)
//                .withObject("key4", new Test("Jack", "Rose"))
                .navigation();
    }


    public static  void startToTopic(String id,String course_id, Activity context , int requestCode){
//        // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
//        ARouter.getInstance().build("/test/activity").navigation();
        // 2. 跳转并携带参数
        ARouter.getInstance().build(ARouterPathConfig.TOPIC_START)
//                .withLong("key1", 666L)
                .withString("c_id", id)//题组id
                .withString("course_id", course_id)//课程id
                .withBoolean("course",true)
//                .withObject("key4", new Test("Jack", "Rose"))
                .navigation(context,requestCode);
    }


    public static  void startToH5Case(String caseid){
//        // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
//        ARouter.getInstance().build("/test/activity").navigation();
        // 2. 跳转并携带参数
        ARouter.getInstance().build(ARouterPathConfig.CASE_START)
//                .withLong("key1", 666L)
                .withString("caseid", caseid)//病例id
//                .withObject("key4", new Test("Jack", "Rose"))
                .navigation();
    }

    public static  void startToDiscuess(String courseId, String courseName){
//        // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
//        ARouter.getInstance().build("/test/activity").navigation();
        // 2. 跳转并携带参数
        ARouter.getInstance().build(ARouterPathConfig.DISCUESS_START)
//                .withLong("key1", 666L)
                .withString("courseId", courseId)//课程id
                .withString("courseName", courseName)//课程名称
                .navigation();
    }


}
