package com.qs.dto.config.ffmpeg;

import com.qs.vo.req.LiveReqVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * FFmpeg推流配置
 *
 * @author FBin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LiveOnlineDTO implements Serializable {

    /**
     * ffmpeg 路径
     */
    private String ffmpegPath;

    /**
     * 视频源
     */
    private String input;

    /**
     * 输出源
     */
    private String output;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 0-推一个元码流；1-推一个自定义推流；2-推两个流（一个是自定义，一个是元码）
     */
    private String twoPart;

    /**
     * 解码类型  默认h264解码
     */
    private String codec;

    /**
     * 转换格式，默认flv
     */
    private String fmt;

    /**
     * -r :帧率，默认25；-g :帧间隔
     */
    private String fps;

    /**
     * -s 分辨率 默认是原分辨率
     */
    private String rs;

    /**
     * 获取实例
     * @param liveReqVO
     * @param ffmpegPath
     * @return
     */
    public static LiveOnlineDTO getInstanceOf(LiveReqVO liveReqVO, String ffmpegPath) {
        if(liveReqVO == null || StringUtils.isBlank(ffmpegPath)){
            return null;
        }
        return LiveOnlineDTO.builder()
                .ffmpegPath(ffmpegPath)
                .input(liveReqVO.getInput())
                .output(liveReqVO.getOutput())
                .appName(liveReqVO.getAppName())
                .twoPart(liveReqVO.getTwoPart())
                .codec(liveReqVO.getCodec())
                .fmt(liveReqVO.getFmt())
                .fps(liveReqVO.getFps())
                .rs(liveReqVO.getRs())
                .build();
    }
}
