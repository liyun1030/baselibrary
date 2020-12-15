package com.common.base.broadcast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.widget.RemoteViews;


import androidx.core.app.NotificationCompat;

import com.common.base.R;
import com.common.base.bean.PushMsgModel;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 消息通知工具类
 */
public class MsgNotificationUtil {
    private static MsgNotificationUtil instance;
    private static final int NOTIFICATION_ID = 0;

    private MsgNotificationUtil() {
    }

    public static MsgNotificationUtil getInstance() {
        if (instance == null) {
            instance = new MsgNotificationUtil();
        }
        return instance;
    }

    /**
     * 创建通知
     */
    public void createNotification(Context ctx, PushMsgModel model) {
        if (model == null) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
        Notification msgNotification = null;
        //这里必须设置chanenelId,要不然该通知在8.0手机上，不能正常显示
        RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.layout_jpush_notification);
        remoteViews.setTextViewText(R.id.titleTxt, model.getContent_title());
        remoteViews.setTextViewText(R.id.descTxt, model.getDesc());
        String channelId = "channel_001";
        //高版本需要渠道
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //只在Android O之上需要渠道
            NotificationChannel notificationChannel = new NotificationChannel(channelId, ctx.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx,channelId);
        builder.setContent(remoteViews);
        msgNotification = builder.build();
        notificationManager.notify(NOTIFICATION_ID, msgNotification);
    }
}
