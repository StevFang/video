package com.qs.service;

import com.qs.dto.config.ffmpeg.LiveOnlineDTO;
import com.qs.vo.req.DecodeHighReqVO;
import com.qs.vo.req.DecodeSimpleReqVO;
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
     * 普清视频转码
     *
     * @param decodeSimpleReqVO
     * @return
     */
    CommonRespVO decodeSimpleVideo(DecodeSimpleReqVO decodeSimpleReqVO);

    /**
     * 高清视频转码
     *
     * @param decodeHighReqVO
     * @return
     */
    CommonRespVO decodeHighVideo(DecodeHighReqVO decodeHighReqVO);

    /**
     * 直播推流
     *
     * @param liveOnlineDTO
     * @return
     */
    CommonRespVO livePushStream(LiveOnlineDTO liveOnlineDTO);
}
