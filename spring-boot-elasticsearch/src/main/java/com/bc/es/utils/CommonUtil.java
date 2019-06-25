package com.bc.es.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

/**
 * 通用工具类
 *
 * @author zhou
 */
public class CommonUtil {
    /**
     * map转对象
     *
     * @param map       map
     * @param beanClass 对象的class
     * @return 对象实体
     */
    public static Object map2Object(Map<String, Object> map, Class<?> beanClass) {
        Object obj;
        try {
            if (map == null) {
                return null;
            }
            obj = beanClass.newInstance();

            BeanUtils.populate(obj, map);

        } catch (Exception e) {
            e.printStackTrace();
            obj = null;
        }
        return obj;
    }
}
