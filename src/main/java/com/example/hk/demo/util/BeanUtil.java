package com.example.hk.demo.util;


import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zr on 2017/8/9.
 */
public class BeanUtil extends cn.hutool.core.bean.BeanUtil {


    public static <T> List<T> convertMap2List(List<Map<String, Object>> list, Class<T> tClass) {
        if (list == null || list.isEmpty() || tClass == null) {
            return null;
        }
        List<T> data = new ArrayList<>();
        for (Map<String, Object> map : list) {
            // T t = mapToBean(map,tClass,true);
            T t = convertMap2Bean(map, tClass);
            data.add(t);
        }
        return data;
    }

    public static <T> T convertMap2Bean(Map<String, Object> map, Class<T> tClass) {
        if (map == null || map.isEmpty() || tClass == null) {
            return null;
        }
        T t = mapToBean(map, tClass, true);
        return t;
    }

    public static Map<String, Object> convertBean2Map(Object o) {
        return beanToMap(o, true, true);
    }

    public static void main(String[] args) {
        System.out.println(StrUtil.toUnderlineCase("UserName"));
        ;
    }
}
