package com.qs.utils;

import org.apache.commons.codec.binary.Base64;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 转换工具类
 * <p>
 * Created by fbin on 2018/5/30.
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
     * 获取两个时间直接的差距
     * @param startTime
     * @param endTime
     * @param type
     * @return
     */
    public static long getInterval(LocalDateTime startTime, LocalDateTime endTime, String type){
        if("y".equals(type)){
            return endTime.getYear() - startTime.getYear();
        }else if("M".equals(type)){
            return (long) ((endTime.getYear() - startTime.getYear()) * 12 +
                    endTime.getMonth().getValue() - startTime.getMonth().getValue());
        }else if("d".equals(type)){
            return endTime.toLocalDate().toEpochDay() - startTime.toLocalDate().toEpochDay();
        }else if("H".equals(type)){
            return (endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() -
                    startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli()) / 1000 / 3600;
        }else if("m".equals(type)){
            return (endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() -
                    startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli()) / 1000 / 60;
        }else if("s".equals(type)){
            return (endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() -
                    startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli()) / 1000;
        }else if("S".equals(type)){
            return (endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() -
                    startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        }
        return 0L;
    }
}
