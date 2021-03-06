package com.qs.service.video;

import com.google.common.collect.Lists;
import com.qs.dto.config.ffmpeg.DecodeDTO;
import com.qs.dto.config.ffmpeg.LiveOnlineDTO;
import com.qs.enums.VideoCodeEnum;
import com.qs.service.VideoService;
import com.qs.service.command.ffmpeg.DecodeHighCommandServiceImpl;
import com.qs.service.command.ffmpeg.DecodeSimpleCommandServiceImpl;
import com.qs.service.command.ffmpeg.LiveOnlineCommandServiceImpl;
import com.qs.service.command.ffmpeg.PlayVideoCommandServiceImpl;
import com.qs.utils.CommonUtils;
import com.qs.vo.req.DecodeHighReqVO;
import com.qs.vo.req.DecodeSimpleReqVO;
import com.qs.vo.req.VideoReqVO;
import com.qs.vo.resp.DecodeRespVO;
import com.qs.vo.resp.LiveRespVO;
import com.qs.vo.resp.CommonRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 视频处理业务类
 *
 * Created by fbin on 2018/5/30.
 *
 * @author FBin
 */
@Slf4j
@Service("videoService")
public class VideoServiceImpl implements VideoService {

    @Value("${server.ffmpeg.path}")
    private String ffmpegPath;

    @Value("${server.upload.filepath}")
    private String savePath;

    @Value("${server.memcoder.path}")
    private String memcoderPath;

    @Autowired
    private LiveOnlineCommandServiceImpl liveOnlineCommandService;

    @Autowired
    private PlayVideoCommandServiceImpl playVideoCommandService;

    @Autowired
    private DecodeSimpleCommandServiceImpl decodeSimpleCommandService;

    @Autowired
    private DecodeHighCommandServiceImpl decodeHighCommandService;

    @Override
    public int findCount(VideoReqVO videoReqVO) {
        return 0;
    }

    @Override
    public List<CommonRespVO> findList(VideoReqVO videoReqVO) {
        return Lists.newArrayList();
    }

    @Override
    public CommonRespVO decodeSimpleVideo(DecodeSimpleReqVO decodeSimpleReqVO) {
        DecodeDTO decodeDTO = DecodeDTO.getInstanceOf(decodeSimpleReqVO, ffmpegPath, memcoderPath, savePath);
        CommonRespVO commonRespVO = this.checkDecodeDTO(decodeDTO);
        if(commonRespVO != null){
            return commonRespVO;
        }
        DecodeRespVO decodeRespVO;
        String decodeCommand = decodeSimpleCommandService.createCommand(decodeDTO);
        if(StringUtils.isNotBlank(decodeCommand)){
            log.info("decode simple command => " + decodeCommand);
            decodeSimpleCommandService.decodeVideo(decodeDTO, decodeCommand);
            decodeRespVO = DecodeRespVO.builder().videoId(decodeDTO.getVideoId()).decodeLog("正在处理中，请稍后").build();
            return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_DECODE_SUCCESS, decodeRespVO);
        }
        decodeRespVO = DecodeRespVO.builder()
                .videoId(decodeSimpleReqVO.getVideoId())
                .decodeLog("普清转码指令未获取到，无法执行").build();
        return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_DECODE_ERROR, decodeRespVO);
    }

    @Override
    public CommonRespVO decodeHighVideo(DecodeHighReqVO decodeHighReqVO) {
        DecodeDTO decodeDTO = DecodeDTO.getInstanceOf(decodeHighReqVO, ffmpegPath, memcoderPath, savePath);
        CommonRespVO commonRespVO = this.checkDecodeDTO(decodeDTO);
        if(commonRespVO != null){
            return commonRespVO;
        }
        DecodeRespVO decodeRespVO;
        String decodeCommand = decodeHighCommandService.createCommand(decodeDTO);
        if(StringUtils.isNotBlank(decodeCommand)){
            log.info("decode high command => " + decodeCommand);
            decodeHighCommandService.decodeVideo(decodeDTO, decodeCommand);
            decodeRespVO = DecodeRespVO.builder().videoId(decodeDTO.getVideoId()).decodeLog("正在处理中，请稍后").build();
            return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_DECODE_SUCCESS, decodeRespVO);
        }
        decodeRespVO = DecodeRespVO.builder()
                .videoId(decodeHighReqVO.getVideoId())
                .decodeLog("高清转码指令未获取到，无法执行").build();
        return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_DECODE_ERROR, decodeRespVO);
    }

    @Override
    public CommonRespVO livePushStream(LiveOnlineDTO liveOnlineDTO) {
        String liveOnlineCommand = liveOnlineCommandService.createCommand(liveOnlineDTO);
        LiveRespVO data;
        if(StringUtils.isNotBlank(liveOnlineCommand)){
            log.info("live video push to server command => " + liveOnlineCommand);
            liveOnlineCommandService.liveOnlineVideo(liveOnlineDTO, liveOnlineCommand);
            data = LiveRespVO.builder()
                    .liveLog("直播推流成功")
                    .output(liveOnlineDTO.getOutput() + liveOnlineDTO.getAppName())
                    .build();
            return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_LIVE_SUCCESS, data);
        }else{
            data = LiveRespVO.builder()
                    .liveLog("直播推流失败，推流指令未获取到，无法执行")
                    .output("")
                    .build();
            return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_LIVE_ERROR, data);
        }
    }

    @Override
    public CommonRespVO playPushStream(LiveOnlineDTO liveOnlineDTO) {
        String playVideoCommand = playVideoCommandService.createCommand(liveOnlineDTO);
        LiveRespVO data;
        if(StringUtils.isNotBlank(playVideoCommand)){
            log.info("play video push to server command => " + playVideoCommand);
            playVideoCommandService.playVideo(liveOnlineDTO, playVideoCommand);
            data = LiveRespVO.builder()
                    .liveLog("点播推流成功")
                    .output("rtmp://localhost:1935/live/" + liveOnlineDTO.getOutput())
                    .build();
            return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_PLAYING_SUCCESS, data);
        }else{
            data = LiveRespVO.builder()
                    .liveLog("点播推流失败，推流指令未获取到，无法执行")
                    .output("")
                    .build();
            return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_PLAYING_ERROR, data);
        }
    }

    /**
     * 校验视频转码参数
     *
     * @param decodeDTO 视频转码参数
     * @return
     */
    private CommonRespVO checkDecodeDTO(DecodeDTO decodeDTO){
        DecodeRespVO decodeRespVO;
        // ffmpeg环境是否配置正确
        if (decodeDTO == null) {
            log.error("配置未正确加载，无法执行");
            decodeRespVO = DecodeRespVO.builder()
                    .videoId(decodeDTO.getVideoId())
                    .decodeLog("配置未正确加载，无法执行").build();
            return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_DECODE_ERROR, decodeRespVO);
        }
        // 参数是否符合要求
        if (StringUtils.isBlank(decodeDTO.getAppName())) {
            log.error("参数不正确，无法执行");
            decodeRespVO = DecodeRespVO.builder()
                    .videoId(decodeDTO.getVideoId())
                    .decodeLog("参数不正确，无法执行").build();
            return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_DECODE_ERROR, decodeRespVO);
        }
        return null;
    }

}
