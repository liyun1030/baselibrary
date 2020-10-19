package com.common.base.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.common.base.R;
import com.common.base.tools.CommUtils;
import com.common.base.tools.Util;
import com.tencent.mm.opensdk.modelmsg.*;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * 微信分享工具类
 */
public class WXUtils {
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    private static final int THUMB_SIZE = 150;
    //发送到朋友圈.如果发送给朋友,更换为SendMessageToWX.Req.WXSceneSession
    private static int SCENE = SendMessageToWX.Req.WXSceneTimeline;

    public static final int TO_FRIEND = 1;
    public static final int TO_CIRCLE = 2;

    private IWXAPI api;

    /**
     * 分享医学知识
     */

    public static void share(Context context, int to, String transaction, final String content, final String... id) {
        final WXUtils wxUtils = new WXUtils(context);
        boolean isInstall = wxUtils.isWXInstalled();
        if (!isInstall) {
            CommUtils.showToast(context, R.string.wx_no_installed);
            return;
        }

        if (TO_FRIEND == to) {
            wxUtils.ifFriend();
        } else if (TO_CIRCLE == to) {
            wxUtils.ifFriendCircle();
        }
        switch (transaction) {
//            case TRANSACTION_EXAM: {
//                wxUtils.sendWebpage(TRANSACTION_EXAM, content, ShareUtils.getShareSelfExaminationTitle(), ShareUtils.getShareSelfExaminationSummary(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_self_examination_share));
//                break;
//            }
//            case TRANSACTION_ESTIMATE_IMAGE: {
//                try {
//                    wxUtils.sendLocalPicture(TRANSACTION_ESTIMATE_IMAGE, content);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//            case TRANSACTION_SEND_CERT: {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            wxUtils.sendLocalCertPicture(TRANSACTION_EXAM_GRADE_RANK, content,id[0]);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                break;
//            }
//            case TRANSACTION_INVITE_FRIEND: {
//                wxUtils.sendWebpage(TRANSACTION_CASE, ShareConstants.APP_DOWNLOAD_HTML_ADDRESS, ShareUtils.getInviteFriendTitle(content), ShareUtils.getInviteFriendSummary(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_app_download_share));
//                break;
//            }
//            case TRANSACTION_EXAM_GRADE_RANK: {
//                try {
//                    wxUtils.sendLocalPicture(TRANSACTION_EXAM_GRADE_RANK, content);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//            case TRANSACTION_INFO_ARTICLE: {
//                String title = id[0];
//                String description = id[1];
//                String imgAddress = id[2];
//                Bitmap bitmap = ImageLoader.getInstance().loadImageSync(imgAddress);
//                if (null == bitmap) {
//                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//                }
//                Bitmap thumbBmp = drawableBitmapOnWhiteBg(context, bitmap);
//                //    Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
//                bitmap.recycle();
//                wxUtils.sendWebpage(TRANSACTION_INFO_ARTICLE, content, title, description, thumbBmp);
//                break;
//            }
//            case TRANSACTION_CASE_CHALLENGE:
//                wxUtils.sendWebpage(TRANSACTION_CASE_CHALLENGE, ShareConstants.APP_DOWNLOAD_HTML_ADDRESS, ShareUtils.getCaseChallengeSummary(), "", BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_app_download_share));
//                break;
//            case TRANSACTION_CASE_REPORT:
//                wxUtils.sendWebpage(TRANSACTION_CASE_REPORT, ShareConstants.APP_CASE_REPORT_ADDRESS + "?" + id[1], ShareUtils.getCaseReportSummary(content), "", BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_app_download_share));
//                break;
        }
    }

    public WXUtils(Context context) {
        api = WXAPIFactory.createWXAPI(context, ShareConstants.WX_APP_ID, false);
        api.registerApp(ShareConstants.WX_APP_ID);
    }
    /**
     * 检查微信是否安装
     */
    public boolean isWXInstalled() {
        return api.isWXAppInstalled();
    }

    public void handleIntent(Intent intent, IWXAPIEventHandler handler) {
        api.handleIntent(intent, handler);
    }

    /**
     * 发送给朋友
     */
    public void ifFriend() {
        SCENE = SendMessageToWX.Req.WXSceneSession;
    }

    /**
     * 发送到朋友圈
     */
    public void ifFriendCircle() {
        SCENE = SendMessageToWX.Req.WXSceneTimeline;
    }

    /**
     * 启动微信
     */
    public boolean openWX() {
        return api.openWXApp();
    }

    /**
     * 检查是否支持发送到朋友圈
     */
    public boolean checkSupportFriendCircle() {
        int wxSdkVersion = api.getWXAppSupportAPI();
        String version = Integer.toHexString(wxSdkVersion);
        return wxSdkVersion >= TIMELINE_SUPPORTED_VERSION;
    }

    public void sendText(String text) {
        if (text == null || text.length() == 0) {
            return;
        }
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        // msg.title = "Will be ignored";
        msg.description = text;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = SCENE;

        // 调用api接口发送数据到微信
        api.sendReq(req);
    }

    /**
     * 发送二进制图片
     * @param bmp
     */
    public void sendBinaryPicture(Bitmap bmp) {
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // 设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 发送本地图片
     * @throws Exception
     */
    public void sendLocalPicture(String transaction, String path) throws Exception {
        File file = new File(path);

        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 3, bmp.getHeight() / 3, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(transaction);
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 发送本地图片
     * @throws Exception
     */
    public void sendLocalCertPicture(String transaction, String path, String title) throws Exception {
        File file = new File(path);

        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.title = title;
        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 3, bmp.getHeight() / 3, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(transaction);
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 发送在线图片
     */
    public void sendOnlinePicture(String url, String title) throws Exception {
        WXImageObject imgObj = new WXImageObject();
        imgObj.imagePath = url;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openStream());
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 3, bmp.getHeight() / 3, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        msg.title = title;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    public Bitmap createBitmapThumbnail(Bitmap bitmap, boolean needRecycler) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 80;
        int newHeight = 80;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitMap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if (needRecycler) bitmap.recycle();
        return newBitMap;
    }

    /**
     * 发送音乐
     */
    public void sendMusic(String music_url, String title, String description, Bitmap thumb) {
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = music_url;
        //music.musicUrl="http://120.196.211.49/XlFNM14sois/AKVPrOJ9CBnIN556OrWEuGhZvlDF02p5zIXwrZqLUTti4o6MOJ4g7C6FPXmtlh6vPtgbKQ==/31353278.mp3";

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = description;
        msg.thumbData = Util.bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 发送低宽带音乐
     */
    public void sendLowBandwidthMusic(String music_url, String title, String description, Bitmap thumb) {
        WXMusicObject music = new WXMusicObject();
        music.musicLowBandUrl = music_url;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = description;

        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 发送视频
     */
    public void sendVideo(String video_url, String title, String description, Bitmap thumb) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = video_url;

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = title;
        msg.description = description;
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 发送低宽带视频
     */
    public void sendLowBandwidthVideo(String video_url, String title, String description, Bitmap thumb) {
        WXVideoObject video = new WXVideoObject();
        video.videoLowBandUrl = video_url;

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = title;
        msg.description = description;
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 发送网页
     */
    public void sendWebpage(String transaction, String web_url, String title, String description, Bitmap thumb) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = web_url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;  //"治趣在线虚拟诊疗平台"
        msg.description = description;  // "互联网医疗教学的全新尝试 通过计算机模拟医学诊疗环境，逼真还原病例处治全程，在生动趣味的益智交互过程中，掌握医学常识，积累临床思维，孕育全科医生"
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(transaction);
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 发送App文件本地路径
     */
    public void sendAppLocalPath(String local_path, String title, String description, String ext_info) {
        WXAppExtendObject appdata = new WXAppExtendObject();
        appdata.filePath = local_path;
        appdata.extInfo = ext_info;

        final WXMediaMessage msg = new WXMediaMessage();
        msg.setThumbImage(Util.extractThumbNail(local_path, 150, 150, true));
        msg.title = title;
        msg.description = description;
        msg.mediaObject = appdata;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("appdata");
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 发送App二进制数据
     */
    public void sendAppBinaryData(String local_path, String title, String description, String ext_info) {
        WXAppExtendObject appdata = new WXAppExtendObject();
        appdata.fileData = Util.readFromFile(local_path, 0, -1);
        appdata.extInfo = ext_info;

        WXMediaMessage msg = new WXMediaMessage();
        msg.setThumbImage(Util.extractThumbNail(local_path, 150, 150, true));
        msg.title = title;
        msg.description = description;
        msg.mediaObject = appdata;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("appdata");
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 发送无附件的App
     */
    public void sendNoAdjunctApp(String title, String description, String ext_info) {
        WXAppExtendObject appdata = new WXAppExtendObject();
        appdata.extInfo = ext_info;
        WXMediaMessage msg = new WXMediaMessage();
        msg.title = title;
        msg.description = description;
        msg.mediaObject = appdata;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("appdata");
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 发送表情本地路径
     */
    public void sendLocalEmoji(String title, String description, String emoji_path, String thumb_path) {
        WXEmojiObject emoji = new WXEmojiObject();
        emoji.emojiPath = emoji_path;

        WXMediaMessage msg = new WXMediaMessage(emoji);
        msg.title = title;
        msg.description = description;
        msg.thumbData = Util.readFromFile(thumb_path, 0, (int) new File(thumb_path).length());


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("emoji");
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 发送表情二进制数据
     */
    public void sendEmojiBinaryData(String title, String description, String emoji_path, String thumb_path) {
        WXEmojiObject emoji = new WXEmojiObject();
        emoji.emojiData = Util.readFromFile(emoji_path, 0, (int) new File(emoji_path).length());
        WXMediaMessage msg = new WXMediaMessage(emoji);

        msg.title = title;
        msg.description = description;
        msg.thumbData = Util.readFromFile(thumb_path, 0, (int) new File(thumb_path).length());

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("emoji");
        req.message = msg;
        req.scene = SCENE;
        api.sendReq(req);
    }

    /**
     * 获取微信访问Token
     */
    public void getWXToken() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "post_timeline";
        req.state = "none";
        api.sendReq(req);
    }

    /**
     * 从微信反注册
     */
    public void unregisterFromWX() {
        api.unregisterApp();
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void detach() {
        api.detach();
    }

    public static void wxLogin(Context context) {
        WXUtils wxUtils = new WXUtils(context);

        boolean isInstall = wxUtils.isWXInstalled();
        if (!isInstall) {
            CommUtils.showToast(context, R.string.wx_no_installed);
            return;
        }

        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        wxUtils.api.sendReq(req);
    }

    /**
     * 把bitmap画到一个白底的newBitmap上,将newBitmap返回
     * @param context 上下文
     * @param bitmap  要绘制的位图
     * @return Bitmap
     */
    public static Bitmap drawableBitmapOnWhiteBg(Context context, Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(context.getResources().getColor(android.R.color.white));
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, 0, 0, paint); //将原图使用给定的画笔画到画布上
        return newBitmap;
    }
}
