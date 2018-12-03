package com.qs.utils;

import com.qs.exception.VideoException;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;

/**
 * 异常处理工具类
 *
 * @author FBin
 * @time 2018/12/3 21:13
 */
public class VideoExceptionUtils {

    public static void assertTrue(boolean flag, String message){
        if(!flag){
            VideoExceptionUtils.fail(message);
        }
    }

    public static void assertNotEmpty(Collection collection, String message){
        if(collection == null || collection.isEmpty()){
            VideoExceptionUtils.fail(message);
        }
    }

    public static void assertNotBlank(String str, String message){
        if(StringUtils.isBlank(str)){
            VideoExceptionUtils.fail(message);
        }
    }

    public static void assertNotNull(Object obj, String message){
        if(obj == null){
            VideoExceptionUtils.fail(message);
        }
    }

    public static void fail(String message){
        throw new VideoException(message);
    }
}
