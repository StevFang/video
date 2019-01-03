package com.qs.controller;

import com.google.common.collect.Lists;
import com.qs.dto.DataTableDTO;
import com.qs.dto.config.FastForwardMovingPictureExpertsGroupLiveDTO;
import com.qs.enums.VideoCodeEnum;
import com.qs.service.video.VideoServiceImpl;
import com.qs.utils.CommonUtils;
import com.qs.vo.resp.DecodeRespVO;
import com.qs.vo.resp.LiveRespVO;
import com.qs.vo.resp.VideoRespVO;
import com.qs.vo.req.DecodeReqVO;
import com.qs.vo.req.LiveReqVO;
import com.qs.vo.req.VideoReqVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 视频服务中心
 *
 * Created by fbin on 2018/5/30.
 *
 * @author FBin
 */
@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("/video")
public class VideoController {

    @Value("${server.ffmpeg.path}")
    private String ffmpegPath;

    @Autowired
    private VideoServiceImpl videoService;

    /**
     * 获取视频展示列表
     * @param videoReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/all", method = { RequestMethod.GET })
    public VideoRespVO findAll(@RequestParam(value = "query", required = false) VideoReqVO videoReqVO){
        try{
            int total = videoService.findCount(videoReqVO);
            List<Map<String, Object>> dataList = Lists.newArrayList();
            if(total > 0){
                dataList = videoService.findList(videoReqVO);
            }
            DataTableDTO dataTableDTO = DataTableDTO.getInstance(videoReqVO, total, dataList);
            VideoRespVO videoRespVO = CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.QUERY_SUCCESS);
            videoRespVO.setData(dataTableDTO);

            return videoRespVO;
        }catch (Exception e){
            log.error("获取视频列表数据异常", e);
            return CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.QUERY_ERROR);
        }
    }

    /**
     * 视频直播
     * @param liveReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/live")
    public VideoRespVO live(LiveReqVO liveReqVO){
        try{
            FastForwardMovingPictureExpertsGroupLiveDTO fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig = FastForwardMovingPictureExpertsGroupLiveDTO.getInstanceOf(liveReqVO, ffmpegPath);
            // ffmpeg环境是否配置正确
            if (fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig == null) {
                return CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.CONFIG_ERROR);
            }
            // 参数是否符合要求
            if (StringUtils.isBlank(fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig.getAppName())) {
                return CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.PARAM_CHECK_ERROR);
            }
            LiveRespVO liveStream = videoService.livePushStream(fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig);

            VideoRespVO videoRespVO = CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.LIVE_SUCCESS);
            videoRespVO.setData(liveStream);

            return videoRespVO;
        }catch (Exception e){
            log.error("视频推流异常", e);
            return CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.LIVE_ERROR);
        }
    }

    /**
     * 视频解码转码
     * @param decodeReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/decode", method = { RequestMethod.POST })
    public VideoRespVO videoDecode(DecodeReqVO decodeReqVO){
        try{
            DecodeRespVO decodeRespVO = videoService.decodeVideo(decodeReqVO);

            VideoRespVO videoRespVO = CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.VIDEO_DECODE_SUCCESS);
            videoRespVO.setData(decodeRespVO);

            return videoRespVO;
        }catch (Exception e){
            log.error("视频解码转码异常", e);

            return CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.VIDEO_DECODE_ERROR);
        }
    }

}
