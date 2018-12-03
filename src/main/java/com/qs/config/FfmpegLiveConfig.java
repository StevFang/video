package com.qs.config;

import com.qs.vo.LiveReqVO;
import lombok.Data;

/**
 * FFmpeg推流配置
 *
 * @author FBin
 */
@Data
public class FfmpegLiveConfig extends AbstractFFmpegConfig {

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

    private FfmpegLiveConfig(){

    }

    /**
     * 获取实例
     * @param liveReqVO
     * @param ffmpegPath
     * @return
     */
    public static FfmpegLiveConfig getInstanceOf(LiveReqVO liveReqVO, String ffmpegPath) {
        FfmpegLiveConfig ffmpegLiveConfig = new FfmpegLiveConfig();
        ffmpegLiveConfig.setFfmpegPath(ffmpegPath);
        ffmpegLiveConfig.setInput(liveReqVO.getInput());
        ffmpegLiveConfig.setOutput(liveReqVO.getOutput());
        ffmpegLiveConfig.setAppName(liveReqVO.getAppName());
        ffmpegLiveConfig.setTwoPart(liveReqVO.getTwoPart());
        ffmpegLiveConfig.setCodec(liveReqVO.getCodec());
        ffmpegLiveConfig.setFmt(liveReqVO.getFmt());
        ffmpegLiveConfig.setFps(liveReqVO.getFps());
        ffmpegLiveConfig.setRs(liveReqVO.getRs());
        return ffmpegLiveConfig;
    }
}
