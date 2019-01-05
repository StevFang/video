package com.qs.utils;

import com.qs.enums.VideoCodeEnum;
import com.qs.vo.resp.CommonRespVO;

/**
 * 通用工具类
 *
 * @author FBin
 * @time 2018/12/3 20:35
 */
public class CommonUtils {

    public static final CommonRespVO getVideoRespVOByCodeEnum(VideoCodeEnum videoCodeEnum){
        return CommonRespVO.builder().code(videoCodeEnum.getCode()).msg(videoCodeEnum.getLabel()).build();
    }

    /**
     * 获取分页页数的算法
     * @param total
     * @param pageSize
     * @return
     */
    public static Integer calPages(int total, int pageSize){
        if(total > 0){
            if(total >= pageSize){
                return Math.floorMod(total, pageSize) > 0 ?
                        Math.floorDiv(total, pageSize) :
                        Math.floorDiv(total, pageSize) + 1;
            }else{
                return 1;
            }
        }else{
            return 0;
        }
    }

}
