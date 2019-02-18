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
     * @param videoReqVO 请求入参
     * @return
     */
    int findCount(VideoReqVO videoReqVO);

    /**
     * 获取视频列表
     *
     * @param videoReqVO 请求入参
     * @return
     */
    List<CommonRespVO> findList(VideoReqVO videoReqVO);

    /**
     * 视频转普清码
     *
     * @param decodeSimpleReqVO 视频转普清码请求参数
     * @return
     */
    CommonRespVO decodeSimpleVideo(DecodeSimpleReqVO decodeSimpleReqVO);

    /**
     * 视频转高清码
     *
     * @param decodeHighReqVO 视频转高清码请求参数
     * @return
     */
    CommonRespVO decodeHighVideo(DecodeHighReqVO decodeHighReqVO);

    /**
     * 直播推流
     *
     * @param liveOnlineDTO 视频直播点播DTO
     * @return
     */
    CommonRespVO livePushStream(LiveOnlineDTO liveOnlineDTO);
}
