package com.common.base.tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Map;
import java.util.Set;

/**
 * android-sp存储
 */
public class SharedPreferencesUtils {
    private static SharedPreferencesUtils spUtil;
    private static SharedPreferences sharedPreferences;

    private SharedPreferencesUtils(Context c, String spName) {
        sharedPreferences = c.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    /**
     * 获得spUtil实例
     *
     * @return spUtil实例
     */
    public static SharedPreferencesUtils getInstance(Context c, String spName) {
        if (spUtil == null) {
            synchronized (SharedPreferencesUtils.class) {
                if (null == spUtil) {
                    spUtil = new SharedPreferencesUtils(c, spName);
                }
            }
        }
        return spUtil;
    }

    private SharedPreferences getPreferences() {
        return sharedPreferences;
    }

    private Editor getEditor() {
        return getPreferences().edit();
    }

    /**
     * 插入键值对
     * @param key   键
     * @param value 值
     */
    public void putString(String key, String value) {
        Editor editor = getEditor();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getParam(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences(BaseConstant.SP_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "").trim();
    }
    public static void saveParam(Context ctx, String key, String value) {
        SharedPreferences pref = ctx.getSharedPreferences(BaseConstant.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static void saveBooleanParam(Context ctx, String key, boolean value) {
        SharedPreferences pref = ctx.getSharedPreferences(BaseConstant.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    /**
     * 插入键值对
     *
     * @param key   键
     * @param value 值
     */
    public void putBoolean(String key, boolean value) {
        Editor editor = getEditor();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 插入键值对
     *
     * @param key   键
     * @param value 值
     */
    public void putFloat(String key, float value) {
        Editor editor = getEditor();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * 插入键值对
     *
     * @param key   键
     * @param value 值
     */
    public void putInt(String key, int value) {
        Editor editor = getEditor();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 插入键值对
     *
     * @param key   键
     * @param value 值
     */
    public void putLong(String key, long value) {
        Editor editor = getEditor();
        editor.putFloat(key, value);
        editor.commit();
    }


    public void putMap(Map<String, String> map) {
        Editor editor = getEditor();
        Set<String> ketSet = map.keySet();
        for (String key : ketSet) {
            editor.putString(key, map.get(key));
        }
        editor.commit();
    }

    /**
     * 插入键值对
     *
     * @param key   键
     * @param value 值
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void putStringSet(String key, Set<String> value) {
        Editor editor = getEditor();
        editor.putStringSet(key, value);
        editor.commit();
    }

    /**
     * 根据键取对应的值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    public String getString(String key, String defaultValue) {
        return getPreferences().getString(key, defaultValue);
    }

    /**
     * 根据键取对应的值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return getPreferences().getBoolean(key, defaultValue);
    }

    /**
     * 根据键取对应的值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    public float getFloat(String key, float defaultValue) {
        return getPreferences().getFloat(key, defaultValue);
    }

    /**
     * 根据键取对应的值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    public int getInt(String key, int defaultValue) {
        return getPreferences().getInt(key, defaultValue);
    }

    /**
     * 根据键取对应的值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    public long getLong(String key, long defaultValue) {
        return getPreferences().getLong(key, defaultValue);
    }

    /**
     * 根据键取对应的值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return getPreferences().getStringSet(key, defaultValue);
    }

    /**
     * 获取所有键值对
     *
     * @return 键值对集合
     */
    public Map<String, ?> getAll() {
        return getPreferences().getAll();
    }

    /**
     * 根据键删除一个键值对
     *
     * @param key 键
     */
    public void remove(String key) {
        Editor editor = getEditor();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清空sp
     */
    public void clear() {
        Editor editor = getEditor();
        editor.clear();
        editor.commit();
    }


    /**
     * 所有键值对中是否包含此键
     *
     * @param key 键
     * @return 是否包含
     */
    public boolean isContainKey(String key) {
        return getAll().containsKey(key);
    }


    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     * @param
     */
    public void setObject(String key, Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            Editor editor = sharedPreferences.edit();
            editor.putString(key, objectVal);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(String key, Class<T> clazz) {
        if (sharedPreferences.contains(key)) {
            String objectVal = sharedPreferences.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 对于外部不可见的过渡方法
     */
    @SuppressWarnings("unchecked")
    private <T> T getValue(String key, Class<T> clazz, SharedPreferences sharedPreferences) {
        T t;
        try {
            t = clazz.newInstance();

            if (t instanceof Integer) {
                return (T) Integer.valueOf(sharedPreferences.getInt(key, 0));
            } else if (t instanceof String) {
                return (T) sharedPreferences.getString(key, "");
            } else if (t instanceof Boolean) {
                return (T) Boolean.valueOf(sharedPreferences.getBoolean(key, false));
            } else if (t instanceof Long) {
                return (T) Long.valueOf(sharedPreferences.getLong(key, 0L));
            } else if (t instanceof Float) {
                return (T) Float.valueOf(sharedPreferences.getFloat(key, 0L));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
