package com.qs.dto.config;

import com.qs.vo.req.DecodeReqVO;
import com.qs.utils.ConvertUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * FFmpeg视频转码配置
 *
 * @author FBin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DecodeffmpegDTO extends BaseffmpegDTO {

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
     * -qscale <数值> 以<数值>质量为基础的VBR，取值0.01-255，约小质量越好
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
     * @param decodeReqVO
     * @param ffmpegPath
     * @param memcoderPath
     * @return
     */
    public static DecodeffmpegDTO getInstanceOf(DecodeReqVO decodeReqVO,
                                                String ffmpegPath,
                                                String memcoderPath,
                                                String savePath) {
        if(decodeReqVO == null
                || StringUtils.isBlank(ffmpegPath)
                || StringUtils.isBlank(memcoderPath)
                || StringUtils.isBlank(savePath)){
            return null;
        }
        return DecodeffmpegDTO.builder()
                .ffmpegPath(ffmpegPath)
                .memcoderPath(memcoderPath)
                .videoId(decodeReqVO.getVideoId())
                .appName(decodeReqVO.getAppName())
                .sourcePath(decodeReqVO.getSourcePath())
                .targetPath(savePath)
                .targetName(ConvertUtil.getFormatUUID())
                .fmt(decodeReqVO.getFmt())
                .bitrate(decodeReqVO.getBitrate())
                .freq(decodeReqVO.getFreq())
                .qscale(decodeReqVO.getQscale())
                .fps(decodeReqVO.getFps())
                .rs(decodeReqVO.getRs())
                .build();
    }
}
