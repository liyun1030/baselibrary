package com.common.base.tool;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * created by 李云 on //
 * 本类的作用:对象与map互转--正则
 */
public class MapUtils {
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass)  {
        if (map == null)
            return null;
        try {
            Object obj = beanClass.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                //获取修饰符，非static静final
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
            return obj;
        }catch (Exception e){
            return null;
        }
    }

    public static Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Object value=field.get(obj);
                if(!EmptyUtil.isObjEmpty(value)) {
                    map.put(field.getName(), String.valueOf(field.get(obj)));
                }
            }
            return map;
        }catch (Exception e){
            return null;
        }
    }
}
