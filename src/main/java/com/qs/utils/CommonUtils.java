package com.qs.utils;

import com.qs.common.CommonConstant;
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

    public static CommonRespVO getCommonRespVO(VideoCodeEnum videoCodeEnum, Object data){
        return CommonRespVO.builder()
                .code(videoCodeEnum.getCode())
                .msg(videoCodeEnum.getLabel())
                .data(data).build();
    }

    /**
     * 获取分页页数的算法
     *
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

    /**
     * 获取全局的oid
     *
     * @return
     */
    public static Long getOid(){
        String key = CommonConstant.PREFIX + CommonConstant.OID_KEY;
        Object contextOid = RedisUtils.get(key);
        if(contextOid == null){
            RedisUtils.set(key, 0);
        }
        return RedisUtils.incr(key, 1);
    }

    /**
     * 获取model类的序号
     *
     * @param tClass
     * @return
     */
    public static Long getModelSeqNo(Class<?> tClass){
        String tClassName = tClass.getSimpleName();
        String key = CommonConstant.PREFIX + tClassName + "_" + CommonConstant.CODE_KEY;
        Object modelSeqNo = RedisUtils.get(key);
        if(modelSeqNo == null){
            RedisUtils.set(key, 0);
        }
        return RedisUtils.incr(key, 1);
    }

    /**
     * 获取model类的时间序号后缀
     *
     * @param tClass
     * @return
     */
    public static String getTimeCodeSuffix(Class<?> tClass){
        String timeCodeSuffix = DateUtils.format(DateUtils.now(), "yyyyMMdd");
        Long modelSeqNo = getModelSeqNo(tClass);
        return timeCodeSuffix + modelSeqNo;
    }

}
