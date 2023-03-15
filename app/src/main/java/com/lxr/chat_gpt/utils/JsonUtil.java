package com.lxr.chat_gpt.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author : Liu XiaoRan
 * @Email : 592923276@qq.com
 * @Date : on 2022/7/14 10:19.
 * @Description :json工具,用于处理传递的参数等
 */
public class JsonUtil {

    /**
     * 将Javabean转换为Map
     *
     * @param javaBean
     *            javaBean
     * @return Map对象
     */
    public static Map<String,Object> toMap(Object javaBean) {

        Map<String,Object> result = new HashMap();
        Method[] methods = javaBean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object value = method.invoke(javaBean, (Object[]) null);
                    result.put(field, null == value ? "" : value.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 将Json对象转换成Map
     *
     * @param jsonString
     *            json对象
     * @return Map对象
     * @throws JSONException
     */
    public static Map toMap(String jsonString){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            Map result = new HashMap();
            Iterator iterator = jsonObject.keys();
            String key;
            String value;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                value = jsonObject.getString(key);
                result.put(key, value);
            }
            return result;
        } catch (JSONException e) {
            return new HashMap();
        }
    }

    /**
     * 将JavaBean转换成JSONObject（通过Map中转）
     *
     * @param bean
     *            javaBean
     * @return json对象
     */
    public static JSONObject toJSON(Object bean) {
        return new JSONObject(toMap(bean));
    }

    /**
     * 将Map转换成Javabean
     *
     * @param javabean
     *            javaBean
     * @param data
     *            Map数据
     */
    public static Object toJavaBean(Object javabean, Map data) {

        Method[] methods = javabean.getClass().getDeclaredMethods();
        for (Method method : methods) {

            try {
                if (method.getName().startsWith("set")) {

                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    method.invoke(javabean, new Object[] {

                            data.get(field)

                    });

                }
            } catch (Exception e) {
            }

        }

        return javabean;

    }

    /**
     * JSONObject到JavaBean
     *
     * @param javabean
     *            javaBean
     * @return json对象
     * @throws ParseException
     *             json解析异常
     * @throws JSONException
     */
    public static void toJavaBean(Object javabean, String jsonString)
            throws ParseException, JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);

        Map map = toMap(jsonObject.toString());

        toJavaBean(javabean, map);

    }
}