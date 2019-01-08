package com.qs.utils;

import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 转换工具类
 *
 * Created by fbin on 2018/5/30.
 *
 * @author FBin
 */
public class ConvertUtil {

    /**
     * Object To Integer
     *
     * @param obj
     * @return
     */
    public static int getInt(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        try{
            if (obj instanceof String) {
                return Integer.parseInt((String) obj);
            }else{
                return Integer.parseInt(obj.toString());
            }
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 获取Base64格式化后的当前时间的字符串
     *
     * @return
     */
    public static String getBase64Time(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssS");
        String localDateTimeStr = LocalDateTime.now().format(dateTimeFormatter);
        return Base64.encodeBase64String(localDateTimeStr.getBytes());
    }

    /**
     * 获取格式化后的UUID
     *
     * @return
     */
    public static String getFormatUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取一个类型的所有属性
     *
     * @param clazz
     * @return
     */
    public static List<Field> getClassFields(Class<?> clazz){
        List<Field> fieldList = Lists.newArrayList();
        Type genericSuperClass = clazz.getGenericSuperclass();
        if(genericSuperClass != null && !genericSuperClass.equals(Object.class)){
            getSuperFields(genericSuperClass.getClass(), fieldList);
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field field : declaredFields){
            fieldList.add(field);
        }
        return fieldList;
    }

    /**
     * 获取父类的所有属性
     *
     * @param clazz
     * @return
     */
    private static void getSuperFields(Class<? extends Type> clazz, List<Field> fieldList) {
        Type genericSuperClass = clazz.getGenericSuperclass();
        if(genericSuperClass != null && !genericSuperClass.equals(Object.class)){
            getSuperFields(clazz, fieldList);
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field field : declaredFields){
            fieldList.add(field);
        }
    }
}
