package com.qs.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 解码转码 View Object
 *
 * Created by fbin on 2018/6/2.
 *
 * @author FBin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecodeSimpleReqVO {

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
     * -q:a <数值> 以<数值>质量为基础的VBR，取值0.01-255，约小质量越好
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

}
