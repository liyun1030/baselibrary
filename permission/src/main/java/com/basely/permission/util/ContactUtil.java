package com.basely.permission.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.basely.permission.bean.ContactBean;

import java.util.ArrayList;

/**
 * 联系人工具类
 */
public class ContactUtil {
    private static ContactUtil instance;
    private Context mContext;

    private ContactUtil(Context context) {
        this.mContext = context;
    }

    public static ContactUtil getInstance(Context context) {
        if (instance == null) {
            instance = new ContactUtil(context);
        }
        return instance;
    }

    /**
     * 查询联系人数据,使用了getContentResolver().query方法来查询系统的联系人的数据
     */
    public ArrayList<ContactBean> readContacts() {
        ArrayList<ContactBean> list=new ArrayList<>();
        Cursor cursor=null;
        try {
            cursor=mContext.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
            //对cursor进行遍历，取出姓名和电话号码
            if (cursor!=null){
                while (cursor.moveToNext()){
                    //获取联系人姓名
                    String displayName=cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                    ));
                    //获取联系人手机号
                    String number=cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));
                    ContactBean contactBean=new ContactBean();
                    contactBean.setUsername(displayName);
                    contactBean.setPhone(number);
                    list.add(contactBean);
                }
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            //记得关掉cursor
            if (cursor!=null){
                cursor.close();
            }
        }
    }
}
