package com.qs.controller;

import com.google.common.collect.Lists;
import com.qs.common.DataTable;
import com.qs.dto.config.FastForwardMovingPictureExpertsGroupLiveConfig;
import com.qs.enums.VideoCodeEnum;
import com.qs.service.video.VideoServiceImpl;
import com.qs.utils.CommonUtils;
import com.qs.vo.*;
import com.qs.vo.ResultVO;
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
    public ResultVO findAll(@RequestParam(value = "query", required = false) VideoReqVO videoReqVO){
        try{
            int total = videoService.findCount(videoReqVO);
            List<Map<String, Object>> dataList = Lists.newArrayList();
            if(total > 0){
                dataList = videoService.findList(videoReqVO);
            }
            DataTable dataTable = DataTable.getInstance(videoReqVO, total, dataList);
            ResultVO resultVO = CommonUtils.getResultVOByCodeEnum(VideoCodeEnum.QUERY_SUCCESS);
            resultVO.setData(dataTable);

            return resultVO;
        }catch (Exception e){
            log.error("获取视频列表数据异常", e);
            return CommonUtils.getResultVOByCodeEnum(VideoCodeEnum.QUERY_ERROR);
        }
    }

    /**
     * 视频直播
     * @param liveReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/live")
    public ResultVO live(LiveReqVO liveReqVO){
        try{
            FastForwardMovingPictureExpertsGroupLiveConfig fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig = FastForwardMovingPictureExpertsGroupLiveConfig.getInstanceOf(liveReqVO, ffmpegPath);
            // ffmpeg环境是否配置正确
            if (fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig == null) {
                return CommonUtils.getResultVOByCodeEnum(VideoCodeEnum.CONFIG_ERROR);
            }
            // 参数是否符合要求
            if (StringUtils.isBlank(fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig.getAppName())) {
                return CommonUtils.getResultVOByCodeEnum(VideoCodeEnum.PARAM_CHECK_ERROR);
            }
            LiveRespVO liveStream = videoService.livePushStream(fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig);

            ResultVO resultVO = CommonUtils.getResultVOByCodeEnum(VideoCodeEnum.LIVE_SUCCESS);
            resultVO.setData(liveStream);

            return resultVO;
        }catch (Exception e){
            log.error("视频推流异常", e);
            return CommonUtils.getResultVOByCodeEnum(VideoCodeEnum.LIVE_ERROR);
        }
    }

    /**
     * 视频解码转码
     * @param decodeReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/decode", method = { RequestMethod.POST })
    public ResultVO videoDecode(DecodeReqVO decodeReqVO){
        try{
            DecodeRespVO decodeRespVO = videoService.decodeVideo(decodeReqVO);

            ResultVO resultVO = CommonUtils.getResultVOByCodeEnum(VideoCodeEnum.VIDEO_DECODE_SUCCESS);
            resultVO.setData(decodeRespVO);

            return resultVO;
        }catch (Exception e){
            log.error("视频解码转码异常", e);

            return CommonUtils.getResultVOByCodeEnum(VideoCodeEnum.VIDEO_DECODE_ERROR);
        }
    }

}
