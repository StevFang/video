package com.qs.service.video;

import com.google.common.collect.Lists;
import com.qs.dto.config.FastForwardMovingPictureExpertsGroupDecodeDTO;
import com.qs.dto.config.FastForwardMovingPictureExpertsGroupLiveDTO;
import com.qs.service.VideoService;
import com.qs.service.manager.FfmpegManagerImpl;
import com.qs.vo.req.DecodeReqVO;
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
    private FfmpegManagerImpl ffmpegManager;

    /**
     * 获取视频总数量
     * @param videoReqVO
     * @return
     */
    @Override
    public int findCount(VideoReqVO videoReqVO) {
        return 0;
    }

    /**
     * 获取视频列表
     * @param videoReqVO
     * @return
     */
    @Override
    public List<CommonRespVO> findList(VideoReqVO videoReqVO) {
        return Lists.newArrayList();
    }

    /**
     * 视频解码转码
     * @param decodeReqVO
     * @return
     */
    @Override
    public DecodeRespVO decodeVideo(DecodeReqVO decodeReqVO) {

        FastForwardMovingPictureExpertsGroupDecodeDTO config =
                FastForwardMovingPictureExpertsGroupDecodeDTO.getInstanceOf(decodeReqVO, ffmpegPath, memcoderPath, savePath);

        // ffmpeg环境是否配置正确
        if (config == null) {
            log.error("配置未正确加载，无法执行");
            return new DecodeRespVO(decodeReqVO.getVideoId(), "配置未正确加载，无法执行");
        }
        // 参数是否符合要求
        if (StringUtils.isBlank(config.getAppName())) {
            log.error("参数不正确，无法执行");
            return new DecodeRespVO(decodeReqVO.getVideoId(), "参数不正确，无法执行");
        }
        ffmpegManager.start(config);

        return new DecodeRespVO(decodeReqVO.getVideoId(), "正在处理中，请稍后");
    }

    /**
     * 直播推流
     * @param fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig
     * @return
     */
    @Override
    public LiveRespVO livePushStream(FastForwardMovingPictureExpertsGroupLiveDTO fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig) {
        ffmpegManager.start(fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig);
        return LiveRespVO.builder().output(fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig.getOutput()).message("推流成功").build();
    }

}
