package com.qs.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 直播推流交互View Object
 *
 * @author FBin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LiveReqVO {

    /**
     * 视频源
     */
    private String input;

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


}
