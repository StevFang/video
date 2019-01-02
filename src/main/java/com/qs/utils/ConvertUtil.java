package com.qs.utils;

import org.apache.commons.codec.binary.Base64;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

}
