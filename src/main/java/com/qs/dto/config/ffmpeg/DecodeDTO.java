package com.qs.dto.config.ffmpeg;

import com.qs.vo.req.DecodeHighReqVO;
import com.qs.vo.req.DecodeSimpleReqVO;
import com.qs.utils.ConvertUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * FFmpeg视频转码配置
 *
 * @author FBin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DecodeDTO implements Serializable {

    /**
     * ffmpeg 路径
     */
    private String ffmpegPath;

    /**
     * memcoder 路径
     */
    private String memcoderPath;

    /**
     * 标识源视频
     */
    private Long videoId;

    /**
     * 源视频名称
     */
    private String appName;

    /**
     * 源视频的绝对路径
     */
    private String sourcePath;

    /**
     * 目标视频存储路径
     */
    private String targetPath;

    /**
     * 目标视频名称
     */
    private String targetName;

    /**
     * 目标视频类型
     */
    private String fmt;

    /**
     * -ab bitrate 音频码率
     */
    private String bitrate;

    /**
     * -ar freq 设置音频采样率
     */
    private String freq;

    /**
     * -qscale <数值> 以<数值>质量为基础的VBR，取值0.01-255，越小质量越好
     */
    private String qscale;

    /**
     * -r 29.97 帧速率（可以改，确认非标准桢率会导致音画不同步，所以只能设定为15或者29.97）
     */
    private String fps;

    /**
     * -s 320x240 指定分辨率
     */
    private String rs;

    /**
     * 获取实例
     * @param decodeSimpleReqVO
     * @param ffmpegPath
     * @param memcoderPath
     * @return
     */
    public static DecodeDTO getInstanceOf(DecodeSimpleReqVO decodeSimpleReqVO,
                                          String ffmpegPath,
                                          String memcoderPath,
                                          String savePath) {
        if(decodeSimpleReqVO == null
                || StringUtils.isBlank(ffmpegPath)
                || StringUtils.isBlank(memcoderPath)
                || StringUtils.isBlank(savePath)){
            return null;
        }
        return DecodeDTO.builder()
                .ffmpegPath(ffmpegPath)
                .memcoderPath(memcoderPath)
                .videoId(decodeSimpleReqVO.getVideoId())
                .appName(decodeSimpleReqVO.getAppName())
                .sourcePath(decodeSimpleReqVO.getSourcePath())
                .targetPath(savePath)
                .targetName(ConvertUtil.getFormatUUID())
                .fmt(decodeSimpleReqVO.getFmt())
                .bitrate(decodeSimpleReqVO.getBitrate())
                .freq(decodeSimpleReqVO.getFreq())
                .qscale(decodeSimpleReqVO.getQscale())
                .fps(decodeSimpleReqVO.getFps())
                .rs(decodeSimpleReqVO.getRs())
                .build();
    }

    public static DecodeDTO getInstanceOf(DecodeHighReqVO decodeHighReqVO,
                                          String ffmpegPath,
                                          String memcoderPath,
                                          String savePath){
        if(decodeHighReqVO == null
                || StringUtils.isBlank(ffmpegPath)
                || StringUtils.isBlank(memcoderPath)
                || StringUtils.isBlank(savePath)){
            return null;
        }
        return DecodeDTO.builder()
                .ffmpegPath(ffmpegPath)
                .memcoderPath(memcoderPath)
                .videoId(decodeHighReqVO.getVideoId())
                .appName(decodeHighReqVO.getAppName())
                .sourcePath(decodeHighReqVO.getSourcePath())
                .targetPath(savePath)
                .targetName(ConvertUtil.getFormatUUID())
                .fmt(decodeHighReqVO.getFmt())
                .bitrate(decodeHighReqVO.getBitrate())
                .freq(decodeHighReqVO.getFreq())
                .qscale(decodeHighReqVO.getQscale())
                .fps(decodeHighReqVO.getFps())
                .rs(decodeHighReqVO.getRs())
                .build();
    }
}
