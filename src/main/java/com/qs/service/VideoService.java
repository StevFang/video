package com.qs.service;

import com.qs.dto.config.FastForwardMovingPictureExpertsGroupLiveDTO;
import com.qs.vo.req.DecodeReqVO;
import com.qs.vo.resp.DecodeRespVO;
import com.qs.vo.resp.LiveRespVO;
import com.qs.vo.req.VideoReqVO;
import com.qs.vo.resp.VideoRespVO;

import java.util.List;
import java.util.Map;

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
    List<VideoRespVO> findList(VideoReqVO videoReqVO);

    /**
     * 视频解码转码
     *
     * @param decodeReqVO
     * @return
     */
    DecodeRespVO decodeVideo(DecodeReqVO decodeReqVO);

    /**
     * 直播推流
     *
     * @param fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig
     * @return
     */
    LiveRespVO livePushStream(FastForwardMovingPictureExpertsGroupLiveDTO fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig);
}
