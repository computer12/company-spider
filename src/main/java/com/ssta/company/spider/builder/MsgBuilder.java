package com.ssta.company.spider.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MsgBuilder {

    /**
     * 包装正常返回的数据
     * @param obj
     * @return
     */
    public static <T> Map<String, Object> buildReturnMessage(T obj){
        Map<String, Object> result = new HashMap<>();
        result.put("retCode", 1);
        result.put("tag", "成功");
        result.put("message", obj);
        return result;
    }

    /**
     * 返回异常提示信息
     * @param msg 异常信息
     * @return
     */
    public static Map<String, Object> buildReturnErrorMessage(String msg){
        Map<String, Object> result = new HashMap<>();
        result.put("retCode", 0);
        result.put("tag", "异常");
        result.put("message", msg);
        return result;
    }


    /**
     * 空值处理-List
     * @param list 需要处理的集合
     * @return
     */
    public static List NullToEmpty(List list){
        return list == null ? new ArrayList() : list;
    }

    /**
     * 空值处理-map
     * @param map 需要处理的map
     * @return
     */
    public static Map NullToEmpty(Map map){
        return map == null ? new HashMap() : map;
    }

    /**
     * 空值处理-String
     * @param str 需要处理的String
     * @return
     */
    public static String NullToEmpty(String str){
        return str == null ? "" : str;
    }
}
