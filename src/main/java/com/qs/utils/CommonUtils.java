package com.qs.utils;

import com.qs.enums.VideoCodeEnum;
import com.qs.ws.ResultInfo;

/**
 * 通用工具类
 *
 * @author FBin
 * @time 2018/12/3 20:35
 */
public class CommonUtils {

    public static final ResultInfo getResultInfoByCodeEnum(VideoCodeEnum videoCodeEnum){
        return ResultInfo.builder().code(videoCodeEnum.getCode()).msg(videoCodeEnum.getLabel()).build();
    }

}
