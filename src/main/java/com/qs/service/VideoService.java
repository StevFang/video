package com.qs.service;

import com.qs.config.FfmpegDecodeConfig;
import com.qs.config.FfmpegLiveConfig;
import com.qs.manager.FfmpegManagerImpl;
import com.qs.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 视频处理业务类
 *
 * Created by fbin on 2018/5/30.
 *
 * @author FBin
 */
@Slf4j
@Service("videoService")
public class VideoService {

    @Value("${server.ffmpeg.path}")
    private String ffmpegPath;

    @Value("${server.upload.filepath}")
    private String savePath;

    @Value("${server.memcoder.path}")
    private String memcoderPath;

    @Autowired
    private FfmpegManagerImpl ffmpegManager;

    /**
     * 获取视频总数量
     * @param videoReqVO
     * @return
     */
    public int findCount(VideoReqVO videoReqVO) {
        return 0;
    }

    /**
     * 获取视频列表
     * @param videoReqVO
     * @return
     */
    public List<Map<String, Object>> findList(VideoReqVO videoReqVO) {
        return new ArrayList<>();
    }

    /**
     * 视频解码转码
     * @param decodeReqVO
     * @return
     */
    public DecodeRespVO decodeVideo(DecodeReqVO decodeReqVO) {

        FfmpegDecodeConfig ffmpegDecodeConfig = FfmpegDecodeConfig.getInstanceOf(decodeReqVO, ffmpegPath, memcoderPath, savePath);

        // ffmpeg环境是否配置正确
        if (ffmpegDecodeConfig == null) {
            log.error("配置未正确加载，无法执行");
            return new DecodeRespVO(decodeReqVO.getVideoId(), "配置未正确加载，无法执行");
        }
        // 参数是否符合要求
        if (StringUtils.isBlank(ffmpegDecodeConfig.getAppName())) {
            log.error("参数不正确，无法执行");
            return new DecodeRespVO(decodeReqVO.getVideoId(), "参数不正确，无法执行");
        }
        ffmpegManager.start(ffmpegDecodeConfig);

        return new DecodeRespVO(decodeReqVO.getVideoId(), "正在处理中，请稍后");
    }

    /**
     * 直播推流
     * @param ffmpegLiveConfig
     * @return
     */
    public LiveRespVO livePushStream(FfmpegLiveConfig ffmpegLiveConfig) {
        ffmpegManager.start(ffmpegLiveConfig);
        return LiveRespVO.builder().output(ffmpegLiveConfig.getOutput()).message("推流成功").build();
    }

}
