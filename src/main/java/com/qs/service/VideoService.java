package com.qs.service;

import com.qs.dto.config.LiveffmpegDTO;
import com.qs.vo.req.DecodeReqVO;
import com.qs.vo.resp.DecodeRespVO;
import com.qs.vo.resp.LiveRespVO;
import com.qs.vo.req.VideoReqVO;
import com.qs.vo.resp.CommonRespVO;

import java.util.List;

/**
 * 视频处理业务接口
 *
 * @author FBin
 * @time 2019/1/2 15:53
 */
public interface VideoService {

    /**
     * 获取视频总数量
     *
     * @param videoReqVO
     * @return
     */
    int findCount(VideoReqVO videoReqVO);

    /**
     * 获取视频列表
     *
     * @param videoReqVO
     * @return
     */
    List<CommonRespVO> findList(VideoReqVO videoReqVO);

    /**
     * 视频解码转码
     *
     * @param decodeReqVO
     * @return
     */
    CommonRespVO decodeVideo(DecodeReqVO decodeReqVO);

    /**
     * 直播推流
     *
     * @param liveffmpegDTO
     * @return
     */
    CommonRespVO livePushStream(LiveffmpegDTO liveffmpegDTO);
}
