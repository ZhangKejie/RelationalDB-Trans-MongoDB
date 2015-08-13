package com.lms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * 解析json工具类。
 * Created by Asus55 on 2015/8/12.
 */
@Component("jsonUtil")
public class JsonUtil {
    /**
     * 解析多层次Json字符串,封装多层Json，避免字符串中有特殊字符而出现的错误
     */
    private final static String regex = "\"([^\\\" ]+?)\":";

    /**
     * 一个方法解析多层json数据  json + 正则 + 递归
     *
     * @param jsonStr
     * @return {@link java.util.Map} or {@link java.util.List} or {@link java.lang.String}
     * @see {@link java.util.regex.Matcher}, {@link java.util.regex.Pattern}
     */
    public static Object jsonParse(final String jsonStr) {
        if (jsonStr == null) throw new NullPointerException("JsonString shouldn't be null");
        try {
            if (isJsonObject(jsonStr)) {  //判断数据为json对象
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(jsonStr);
                final Map<String, Object> map = new HashMap<String, Object>();
                final JSONObject jsonObject = new JSONObject(jsonStr);
                try {
                    for (; matcher.find(); ) {//循坏查找符合条件的key
                        String groupName = matcher.group(1);     //利用正在表达式提取出json对象的对象名
                        Object obj = jsonObject.opt(groupName);
                        if (isJsonObject(obj + "") || isJsonArray(obj + "")) {
                            matcher.region(matcher.end() + (obj + "").replace("\\", "").length(), matcher.regionEnd());//设定搜索范围
                            map.put(groupName, jsonParse(obj + ""));//解析内存json对象
                        } else {
                            map.put(groupName, obj + "");//若内层已无json对象直接放入map
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                return map;
            } else if (isJsonArray(jsonStr)) {//判断数据为json数组
                List<Object> list = new ArrayList<Object>();
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {//循环解析json数组
                        Object object = jsonArray.opt(i);
                        list.add(jsonParse(object + ""));
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                return list;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * To determine whether a string is JsonObject {@link org.json.JSONObject}
     *
     * @param jsonStr {@link java.lang.String}
     * @return boolean
     */
    private static boolean isJsonObject(final String jsonStr) {//利用正则表达式判断该数据是否为json对象
        if (jsonStr == null) return false;
        return Pattern.matches("^\\{.*\\}$", jsonStr.trim());
    }

    /**
     * To determine whether a string is JsonArray {@link org.json.JSONArray};
     *
     * @param jsonStr {@link java.lang.String}
     * @return boolean
     */
    private static boolean isJsonArray(final String jsonStr) {//根据正则表达式判断该数据为json数组
        if (jsonStr == null) return false;
        return Pattern.matches("^\\[.*\\]$", jsonStr.trim());
    }
}

