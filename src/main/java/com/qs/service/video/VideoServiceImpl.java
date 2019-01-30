package com.qs.service.video;

import com.google.common.collect.Lists;
import com.qs.dto.config.DecodeffmpegDTO;
import com.qs.dto.config.LiveffmpegDTO;
import com.qs.enums.VideoCodeEnum;
import com.qs.service.VideoService;
import com.qs.service.manager.FfmpegManagerImpl;
import com.qs.utils.CommonUtils;
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
     *
     * @param decodeReqVO
     * @return
     */
    @Override
    public CommonRespVO decodeVideo(DecodeReqVO decodeReqVO) {
        DecodeffmpegDTO config = DecodeffmpegDTO.getInstanceOf(decodeReqVO, ffmpegPath, memcoderPath, savePath);
        DecodeRespVO decodeRespVO;
        // ffmpeg环境是否配置正确
        if (config == null) {
            log.error("配置未正确加载，无法执行");
            decodeRespVO = DecodeRespVO.builder()
                    .videoId(decodeReqVO.getVideoId())
                    .decodeLog("配置未正确加载，无法执行").build();
            return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_DECODE_ERROR, decodeRespVO);
        }
        // 参数是否符合要求
        if (StringUtils.isBlank(config.getAppName())) {
            log.error("参数不正确，无法执行");
            decodeRespVO = DecodeRespVO.builder()
                    .videoId(decodeReqVO.getVideoId())
                    .decodeLog("参数不正确，无法执行").build();
            return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_DECODE_ERROR, decodeRespVO);
        }
        ffmpegManager.start(config);
        decodeRespVO = DecodeRespVO.builder()
                .videoId(decodeReqVO.getVideoId())
                .decodeLog("正在处理中，请稍后").build();
        return CommonUtils.getCommonRespVO(VideoCodeEnum.VIDEO_DECODE_SUCCESS, decodeRespVO);
    }

    /**
     * 直播推流
     *
     * @param liveffmpegDTO
     * @return
     */
    @Override
    public CommonRespVO livePushStream(LiveffmpegDTO liveffmpegDTO) {
        ffmpegManager.start(liveffmpegDTO);
        LiveRespVO data = LiveRespVO.builder().output(liveffmpegDTO.getOutput() + liveffmpegDTO.getAppName()).build();
        return CommonUtils.getCommonRespVO(VideoCodeEnum.LIVE_SUCCESS, data);
    }

}
