package com.qs.utils;

import com.qs.enums.VideoCodeEnum;
import com.qs.vo.ResultVO;

/**
 * 通用工具类
 *
 * @author FBin
 * @time 2018/12/3 20:35
 */
public class CommonUtils {

    public static final ResultVO getResultVOByCodeEnum(VideoCodeEnum videoCodeEnum){
        return ResultVO.builder().code(videoCodeEnum.getCode()).msg(videoCodeEnum.getLabel()).build();
    }

}
