package com.common.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.common.base.bean.PushMsgModel;
import com.common.base.broadcast.MsgNotificationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";
    private static Object tag;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String registrationID = JPushInterface.getRegistrationID(context);
//			Log.d(TAG, "设备id : " + registrationID);
            Bundle bundle = intent.getExtras();
            Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
//			RxBus.get().post(RXTagConstant.JPUSH_MESSAGE,"");
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//				processCustomMessage(context, bundle);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                jumpActivity(context, bundle);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
                goActivity(context, bundle);
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    private void goActivity(Context ctx, Bundle bundle){
        PushMsgModel model = new PushMsgModel();
        model.setContent_title(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
        model.setDesc(bundle.getString(JPushInterface.EXTRA_ALERT));
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next();
                        String value = "";
                        value = json.optString(myKey);
                        switch (myKey) {
                            case "activity_id": {
                                model.setActivity_id(value);
                                break;
                            }
                            case "h_id": {
                                model.setH_id(value);
                                break;
                            }
                            case "msg_id": {
                                model.setMsg_id(value);
                                break;
                            }
                            case "mt_id": {
                                model.setMt_id(value);
                                break;
                            }
                            case "url": {
                                model.setUrl(value);
                                break;
                            }
                            case "rd_id": {
                                model.setRd_id(value);
                                break;
                            }
                            case "rud_id": {
                                model.setRud_id(value);
                                break;
                            }
                            case "is_approval": {
                                model.setIs_approval(Integer.parseInt(value));
                                break;
                            }
                            case "department_name": {
                                model.setDepartment_name(value);
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                }
            }
        }
//        Intent intent = null;
//        if (!TextUtils.isEmpty(model.getUrl())) {
//            String url = model.getUrl();
//            if (url.equals("1014")) {
//                //预约详情--比较多
//                //分项目或房间--技能中心 暂时不调试
//            } else if (url.equals("1015")) {
//                //学员培训内容审批列表--老师
//                intent = new Intent(ctx, TeacherContentAuditActivity.class);
//                intent.putExtra("rdId", model.getRd_id());
//            } else if (url.equals("1016")) {
//                //教学活动详情--老师
//                intent = new Intent(ctx, ResidentTrainingDetailActivity.class);
//                intent.putExtra("activity_id", model.getActivity_id());
//                intent.putExtra("from", 2);
//                intent.putExtra("state", 2);
//
//                if(model.getMt_id().equals("10167")){
//                    //10167是讲师 一个是邀约
//                    intent.putExtra("from", 2);//1为学生端 2为教师端
//                    intent.putExtra("state", 1);// 1我的授课 2参与活动
//                }else if(model.getMt_id().equals("10168")){
//                    intent.putExtra("from", 2);//1为学生端 2为教师端
//                    intent.putExtra("state", 2);// 1我的授课 2参与活动
//                }
//
//            } else if (url.equals("1017")) {
//                //教师端-出科详情 全部跳列表
////                if (TextUtils.equals("出科申请未通过", model.getContent_title())) {
////                    //仅查看
////                    intent = new Intent(ctx, TeacherStuOutDepOnlySeeActivity.class);
////                    intent.putExtra("isApproval", model.getIs_approval());
////                    intent.putExtra("rud_id", model.getRud_id());
////                } else {
////                    //编辑提交
////                    intent = new Intent(ctx, TeacherStuOutDepActivity.class);
////                    intent.putExtra("isApproval", model.getIs_approval());
////                    intent.putExtra("rud_id", model.getRud_id());
////                }
//                intent = new Intent(ctx, TeacherOutDepartmentActivity.class);
//                intent.putExtra("isApproval", model.getIs_approval());
//                intent.putExtra("rdid", model.getRd_id());
//            } else if (url.equals("1018")) {
//                //我的参与考试列表--技能考试
//                intent = new Intent(ctx, SkillExamActivity.class);
//            } else if (url.equals("1019")) {
//                //轮转详情
//                //状态 1.待入科2.已入科3.已出科
//                intent = new Intent(ctx, PlanDetailActivity.class);
//                OrgPlanItemBean panBean = new OrgPlanItemBean();
//                panBean.setState(2);
//                panBean.setRud_id(model.getRud_id());
//                intent.putExtra("bean", panBean);
//            } else if (url.equals("1020")) {
//                //全部轮转计划
//                ThirdLoginByResModel.DataBean.OrgsBean curOrg = MyApplication.getInstance().getCurOrg();
//                if (curOrg != null && !TextUtils.isEmpty(curOrg.getUe_identity()) && TextUtils.equals(curOrg.getUe_identity(),"2")) {
//                    //1 住院医师 2 实习医生-本科生 3 在校学生
//                    intent = new Intent(ctx, UndergraduateAllPlanActivity.class);
//                } else {
//                    intent = new Intent(ctx, StandAllPlanActivity.class);
//                }
//            } else if (url.equals("1021")) {
//                //状态 1.待入科2.已入科3.已出科
//                intent = new Intent(ctx, PlanDetailActivity.class);
//                OrgPlanItemBean panBean = new OrgPlanItemBean();
//                panBean.setState(1);
//                panBean.setRud_id(model.getRud_id());
//                intent.putExtra("bean", panBean);
//            } else if (url.equals("1022")) {
//                //教学活动详情--学生
//                intent = new Intent(ctx, ResidentTrainingDetailActivity.class);
//                intent.putExtra("activity_id", model.getActivity_id());
//                intent.putExtra("from", 1);
//                intent.putExtra("state", 2);
//            } else if (url.equals("1023") || url.equals("1024")) {
//                //出科成绩、出科详情
//                int userIdentity = MyApplication.getInstance().getUserIdentity();
//                if (userIdentity == 1) { //1学生
//                    ThirdLoginByResModel.DataBean.OrgsBean curOrg = MyApplication.getInstance().getCurOrg();
//                    if (curOrg != null && !TextUtils.isEmpty(curOrg.getUe_identity()) && TextUtils.equals(curOrg.getUe_identity(), "2")) {
//                        //1 住院医师 2 实习医生本科生 3 在校学生
//                        //1有成绩 2有成绩需要评价老师可看 3暂无成绩 4需要申请
//                        intent = new Intent(ctx, ViewUndergraduateCheckScoreActivity.class);
//                        intent.putExtra("roundId", model.getRud_id());
//                        intent.putExtra("department_name", model.getRd_name());
//                        intent.putExtra("rd_id", model.getRd_id());
//                    } else {
//                        //出科考核查看成绩页,出科考核-已评分,规培
//                        intent = new Intent(ctx, CheckingOutDetailActivity.class);
//                        intent.putExtra("rd_id", model.getRd_id());
//                        intent.putExtra("rd_name", model.getRd_name());
//                        intent.putExtra("rud_id", model.getRud_id());
//                        intent.putExtra("role", 0);
//                    }
//                }
//            }
//        }
//        //remoteViews的意图
//        int userIdentity = MyApplication.getInstance().getUserIdentity();
//        if (intent == null) {
//            if (userIdentity == 1) {//1学生
//                intent = new Intent(ctx, OrganHomeActivity.class);
//            } else if (userIdentity == 2) {//2老师
//                intent = new Intent(ctx, com.curefun.activity.largeplatform.forteacher.OrganHomeActivity.class);
//            }
//        }
//        ctx.startActivity(intent);
    }
    private void jumpActivity(Context ctx, Bundle bundle) {
        PushMsgModel model = new PushMsgModel();
        model.setContent_title(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
        model.setDesc(bundle.getString(JPushInterface.EXTRA_ALERT));
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next();
                        String value = "";
                        value = json.optString(myKey);
                        switch (myKey) {
                            case "activity_id": {
                                model.setActivity_id(value);
                                break;
                            }
                            case "h_id": {
                                model.setH_id(value);
                                break;
                            }
                            case "msg_id": {
                                model.setMsg_id(value);
                                break;
                            }
                            case "mt_id": {
                                model.setMt_id(value);
                                break;
                            }
                            case "url": {
                                model.setUrl(value);
                                break;
                            }
                            case "rd_id": {
                                model.setRd_id(value);
                                break;
                            }
                            case "rud_id": {
                                model.setRud_id(value);
                                break;
                            }
                            case "is_approval": {
                                model.setIs_approval(Integer.parseInt(value));
                                break;
                            }
                            case "department_name": {
                                model.setDepartment_name(value);
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                }
            }
        }
        MsgNotificationUtil.getInstance().createNotification(ctx, model);
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        if (myKey.equals("url")) tag = json.optString(myKey);
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
//	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//		}
//	}
}
