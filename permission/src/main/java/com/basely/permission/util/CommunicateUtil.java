package com.basely.permission.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.telecomyt.permission.bean.CommunicateBean;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.TextUtils;

/**
 * 通话管理
 */
public class CommunicateUtil {
    private static volatile CommunicateUtil instance;
    private Context context;

    private CommunicateUtil(Context ctx) {
        this.context = ctx;
    }

    private Uri callUri = CallLog.Calls.CONTENT_URI;
    private String[] columns = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE};// 通话类型}

    public static CommunicateUtil getInstance(Context ctx) {
        if (instance == null) {
            synchronized (CommunicateUtil.class) {
                if (instance == null) {
                    instance = new CommunicateUtil(ctx);
                }
            }
        }
        return instance;
    }

    private void addCallLOg() {  //添加通话记录
        ContentValues values = new ContentValues();
        values.clear();
        values.put(CallLog.Calls.CACHED_NAME, "lum");
        values.put(CallLog.Calls.NUMBER, 123456789);
        values.put(CallLog.Calls.TYPE, "1");
/*        values.put(CallLog.Calls.DATE, calllog.getmCallLogDate());
        values.put(CallLog.Calls.DURATION, calllog.getmCallLogDuration());*/
        values.put(CallLog.Calls.NEW, "0");// 0已看1未看 ,由于没有获取默认全为已读
        context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
    }

    //获取通话记录
    public ArrayList<CommunicateBean> getContentCallLog() {
        Cursor cursor = context.getContentResolver().query(callUri, // 查询通话记录的URI
                columns
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        ArrayList<CommunicateBean> beans = new ArrayList<CommunicateBean>();
        CommunicateBean bean = null;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //姓名
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)); //获取通话日期
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));//获取通话时长，值为多少秒
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)); //获取通话类型：1.呼入2.呼出3.未接
            String dayCurrent = new SimpleDateFormat("dd").format(new Date());
            String dayRecord = new SimpleDateFormat("dd").format(new Date(dateLong));

            bean = new CommunicateBean();
            if(TextUtils.isEmpty(name)){
                bean.setName(number);
            }else{
                bean.setName(name);
            }
            bean.setNumber(number);
            bean.setDate(date);
            bean.setTime(time);
            bean.setDuration(duration);
            bean.setType(type);
            beans.add(bean);
        }
        return beans;
    }

    /**
     * 获得收藏夹的联系人
     */
    public ArrayList<CommunicateBean> getKeepedContacts() {
        Cursor cur = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts.STARRED + " = 1 ", null, null);
//        ((Activity)(context)).startManagingCursor(cur);
        int count = 0;
        ArrayList<CommunicateBean> beans = new ArrayList<>();
        while (cur.moveToNext()) {
            count++;

            long id = cur.getLong(cur.getColumnIndex("_id"));
            Cursor pcur = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + Long.toString(id), null, null);

            // 处理多个号码的情况
            String phoneNumbers = "";
            while (pcur.moveToNext()) {
                String strPhoneNumber = pcur
                        .getString(pcur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if(TextUtils.isEmpty(phoneNumbers)){
                    phoneNumbers += strPhoneNumber;
                }else {
                    phoneNumbers += strPhoneNumber + ":";
                }
            }
            phoneNumbers += "\n";
            pcur.close();
            String name = cur.getString(cur.getColumnIndex("display_name"));
            CommunicateBean bean = new CommunicateBean();
            bean.setName(name);
            bean.setNumber(phoneNumbers);
            beans.add(bean);
        }
        cur.close();
        return beans;
    }

    /**
     * 获取联系人列表
     */
    public ArrayList<CommunicateBean> getContactList2() {
        //获取所有联系人
        ArrayList<CommunicateBean> beans = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        // 联系人姓名
        String NAME = ContactsContract.Contacts.DISPLAY_NAME;
        // 号码
        String NUM = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Cursor cur = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts.STARRED + " = 1 ", null, null);

//        Cursor pcur = cr.query(
//                phoneUri,
//                null,
//                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
//                        + Long.toString(id), null, null);
        while (cursor.moveToNext()) {
            CommunicateBean bean = new CommunicateBean();
            bean.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            bean.setNumber(cursor.getString(cursor.getColumnIndex(NUM)));
            beans.add(bean);
        }
        return beans;
    }
    /*
     * 读取联系人的信息
     */
    public ArrayList<CommunicateBean> getContactList() {
        Cursor cursor = this.context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;

        if (cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        ArrayList<CommunicateBean> beans = new ArrayList<>();
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);
            CommunicateBean bean = new CommunicateBean();
            bean.setName(name);
            /*
             * 查找该联系人的phone信息
             */
            Cursor phones = this.context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            // 处理多个号码的情况
            String phoneNumbers = "";
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex);
                String strPhoneNumber = phones.getString(phoneIndex);
                if(TextUtils.isEmpty(phoneNumbers)){
                    phoneNumbers += strPhoneNumber;
                }else {
                    phoneNumbers += strPhoneNumber + ":";
                }
            }
            phoneNumbers += "\n";
            bean.setNumber(phoneNumbers);
            beans.add(bean);
        }
        return beans;
    }
    /**
     * 添加到收藏夹
     */
    @SuppressWarnings("deprecation")
    private void addKeepedContacts(long _id) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cusor = null;
        @SuppressWarnings("deprecation")
        String[] projection = new String[]{Contacts.People._ID, Contacts.People.NAME, Contacts.People.NUMBER};
        cusor = contentResolver.query(Contacts.People.CONTENT_URI, projection, Contacts.People._ID + "=?", new String[]{_id + ""}, Contacts.People.NAME + " ASC");
        cusor.moveToFirst();
        ContentValues values = new ContentValues();
        Uri uri = Uri.withAppendedPath(Contacts.People.CONTENT_URI, cusor.getString(cusor.getColumnIndex(Contacts.People._ID)));
// values.put(Contacts.People.NAME, newName);
        values.put(Contacts.People.STARRED, 1);
// values.put(Contacts.Phones.NUMBER, newPhoneNum);
        contentResolver.update(uri, values, null, null);
    }

    /**
     * 从收藏夹中移出
     */
    @SuppressWarnings("deprecation")
    private void removeKeepedContacts(long _id) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cusor = null;
        @SuppressWarnings("deprecation")
        String[] projection = new String[]{Contacts.People._ID, Contacts.People.NAME, Contacts.People.NUMBER};
        cusor = contentResolver.query(Contacts.People.CONTENT_URI, projection, Contacts.People._ID + "=?", new String[]{_id + ""}, Contacts.People.NAME + " ASC");
        cusor.moveToFirst();
        ContentValues values = new ContentValues();
        Uri uri = Uri.withAppendedPath(Contacts.People.CONTENT_URI, cusor.getString(cusor.getColumnIndex(Contacts.People._ID)));
        // values.put(Contacts.People.NAME, newName);
        values.put(Contacts.People.STARRED, 0);
        // values.put(Contacts.Phones.NUMBER, newPhoneNum);
        contentResolver.update(uri, values, null, null);
    }
}
