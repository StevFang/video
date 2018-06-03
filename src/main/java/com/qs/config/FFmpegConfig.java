package com.qs.config;

import com.qs.form.DecodeForm;
import lombok.Data;

/**
 * FFmpeg配置
 */
@Data
public class FFmpegConfig {

    // ffmpeg 路径
    private String ffmpegPath;

    // 视频源
    private String input;

    // 输出源
    private String output;

    // 应用名
    private String appName;

    // 0-推一个元码流；1-推一个自定义推流；2-推两个流（一个是自定义，一个是元码）
    private String twoPart;

    // 解码类型  默认h264解码
    private String codec;

    // 转换格式，默认flv
    private String fmt;

    // -r :帧率，默认25；-g :帧间隔
    private String fps;

    // -s 分辨率 默认是原分辨率
    private String rs;

    private FFmpegConfig(){

    }

    /**
     * 获取实例
     * @param decodeForm
     * @param ffmpegPath
     * @return
     */
    public static FFmpegConfig getInstanceOf(DecodeForm decodeForm, String ffmpegPath) {
        FFmpegConfig ffmpegConfig = new FFmpegConfig();
        ffmpegConfig.setFfmpegPath(ffmpegPath);
        ffmpegConfig.setInput(decodeForm.getInput());
        ffmpegConfig.setOutput(decodeForm.getOutput());
        ffmpegConfig.setAppName(decodeForm.getAppName());
        ffmpegConfig.setTwoPart(decodeForm.getTwoPart());
        ffmpegConfig.setCodec(decodeForm.getCodec());
        ffmpegConfig.setFmt(decodeForm.getFmt());
        ffmpegConfig.setFps(decodeForm.getFps());
        ffmpegConfig.setRs(decodeForm.getRs());
        return ffmpegConfig;
    }
}
