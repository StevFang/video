package com.qs.controller;

import com.google.common.collect.Lists;
import com.qs.common.DataTable;
import com.qs.config.FfmpegLiveConfig;
import com.qs.enums.VideoCodeEnum;
import com.qs.service.VideoService;
import com.qs.utils.CommonUtils;
import com.qs.vo.*;
import com.qs.ws.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private VideoService videoService;

    /**
     * 获取视频展示列表
     * @param videoReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/all", method = { RequestMethod.GET })
    public ResultInfo findAll(@RequestParam(value = "query", required = false) VideoReqVO videoReqVO){
        try{
            int total = videoService.findCount(videoReqVO);
            List<Map<String, Object>> dataList = Lists.newArrayList();
            if(total > 0){
                dataList = videoService.findList(videoReqVO);
            }
            DataTable dataTable = DataTable.getInstance(videoReqVO, total, dataList);
            ResultInfo resultInfo = CommonUtils.getResultInfoByCodeEnum(VideoCodeEnum.QUERY_SUCCESS);
            resultInfo.setData(dataTable);

            return resultInfo;
        }catch (Exception e){
            log.error("获取视频列表数据异常", e);
            return CommonUtils.getResultInfoByCodeEnum(VideoCodeEnum.QUERY_ERROR);
        }
    }

    /**
     * 视频直播
     * @param liveReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/live")
    public ResultInfo live(LiveReqVO liveReqVO){
        try{
            FfmpegLiveConfig ffmpegLiveConfig = FfmpegLiveConfig.getInstanceOf(liveReqVO, ffmpegPath);
            // ffmpeg环境是否配置正确
            if (ffmpegLiveConfig == null) {
                return CommonUtils.getResultInfoByCodeEnum(VideoCodeEnum.CONFIG_ERROR);
            }
            // 参数是否符合要求
            if (StringUtils.isBlank(ffmpegLiveConfig.getAppName())) {
                return CommonUtils.getResultInfoByCodeEnum(VideoCodeEnum.PARAM_CHECK_ERROR);
            }
            LiveRespVO liveStream = videoService.livePushStream(ffmpegLiveConfig);

            ResultInfo resultInfo = CommonUtils.getResultInfoByCodeEnum(VideoCodeEnum.LIVE_SUCCESS);
            resultInfo.setData(liveStream);

            return resultInfo;
        }catch (Exception e){
            log.error("视频推流异常", e);
            return CommonUtils.getResultInfoByCodeEnum(VideoCodeEnum.LIVE_ERROR);
        }
    }

    /**
     * 视频解码转码
     * @param decodeReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/decode", method = { RequestMethod.POST })
    public ResultInfo videoDecode(DecodeReqVO decodeReqVO){
        try{
            DecodeRespVO decodeRespVO = videoService.decodeVideo(decodeReqVO);

            ResultInfo resultInfo = CommonUtils.getResultInfoByCodeEnum(VideoCodeEnum.VIDEO_DECODE_SUCCESS);
            resultInfo.setData(decodeRespVO);

            return resultInfo;
        }catch (Exception e){
            log.error("视频解码转码异常", e);

            return CommonUtils.getResultInfoByCodeEnum(VideoCodeEnum.VIDEO_DECODE_ERROR);
        }
    }

}
