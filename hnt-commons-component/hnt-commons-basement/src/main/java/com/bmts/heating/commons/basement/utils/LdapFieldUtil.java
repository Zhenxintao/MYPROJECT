package com.bmts.heating.commons.basement.utils;

import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: LdapFieldUtil
 * @Description: Ldap数据解析
 * @Author: pxf
 * @Date: 2020/9/27 14:05
 * @Version: 1.0
 */
@Slf4j
public class LdapFieldUtil {

    public static Map<String, String> getFields(Class<?> type, Object obj) {
        return initField(type, obj);
    }

    private static Map<String, String> initField(Class<?> type, Object obj) {
        Field[] fields = type.getDeclaredFields();
        Map<String, String> map = new HashMap<>();
        for (Field field : fields) {
            // 排除静态字段，解决bug#2
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    if (field.get(obj) != null) {
                        map.put(field.getName(), field.get(obj).toString());
                    } else {
                        map.put(field.getName(), null);
                    }
                } catch (Exception e) {
                    log.info("initField fail fieldName={}, Exception={}", field.getName(), e);

                }
            }
        }
        return map;
    }


    /**
     * 将驼峰风格替换为下划线风格
     */
    public static String camelhumpToUnderline(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
    }

    /**
     * 将下划线风格替换为小驼峰风格
     */
    public static String underlineToCamelhump(String str) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }

}
