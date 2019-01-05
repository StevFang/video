package com.qs.controller;

import com.google.common.collect.Lists;
import com.qs.dto.common.DataTableDTO;
import com.qs.dto.config.FastForwardMovingPictureExpertsGroupLiveDTO;
import com.qs.enums.VideoCodeEnum;
import com.qs.service.VideoService;
import com.qs.utils.CommonUtils;
import com.qs.vo.req.DecodeReqVO;
import com.qs.vo.req.LiveReqVO;
import com.qs.vo.req.VideoReqVO;
import com.qs.vo.resp.DecodeRespVO;
import com.qs.vo.resp.LiveRespVO;
import com.qs.vo.resp.CommonRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public CommonRespVO findAll(@RequestParam(value = "query", required = false) VideoReqVO videoReqVO){
        try{
            int total = videoService.findCount(videoReqVO);
            List<CommonRespVO> dataList = Lists.newArrayList();
            if(total > 0){
                dataList = videoService.findList(videoReqVO);
            }
            DataTableDTO dataTableDTO = DataTableDTO.builder()
                    .total(total)
                    .current(videoReqVO.getCurrent())
                    .pageSize(videoReqVO.getPageSize())
                    .pages(CommonUtils.calPages(total, videoReqVO.getPageSize()))
                    .dataList(dataList)
                    .build();
            CommonRespVO commonRespVO = CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.QUERY_SUCCESS);
            commonRespVO.setData(dataTableDTO);

            return commonRespVO;
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
    public CommonRespVO live(LiveReqVO liveReqVO){
        try{
            CommonRespVO commonRespVO;
            // 参数是否符合要求
            if (StringUtils.isBlank(liveReqVO.getAppName())) {
                commonRespVO = CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.PARAM_CHECK_ERROR);
                commonRespVO.setMsg(String.format(commonRespVO.getMsg(), "appName", liveReqVO.getAppName()));
                return commonRespVO;
            }

            FastForwardMovingPictureExpertsGroupLiveDTO config =
                    FastForwardMovingPictureExpertsGroupLiveDTO.getInstanceOf(liveReqVO, ffmpegPath);
            // ffmpeg环境是否配置正确
            if (config == null) {
                commonRespVO = CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.CONFIG_ERROR);
                return commonRespVO;
            }

            LiveRespVO liveStream = videoService.livePushStream(config);
            commonRespVO = CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.LIVE_SUCCESS);
            commonRespVO.setData(liveStream);

            return commonRespVO;
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
    public CommonRespVO videoDecode(DecodeReqVO decodeReqVO){
        try{
            DecodeRespVO decodeRespVO = videoService.decodeVideo(decodeReqVO);

            CommonRespVO commonRespVO = CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.VIDEO_DECODE_SUCCESS);
            commonRespVO.setData(decodeRespVO);

            return commonRespVO;
        }catch (Exception e){
            log.error("视频解码转码异常", e);

            return CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.VIDEO_DECODE_ERROR);
        }
    }

}
