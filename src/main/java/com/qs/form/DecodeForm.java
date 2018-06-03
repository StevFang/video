package com.qs.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 解码转码表单信息
 *
 * Created by fbin on 2018/6/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecodeForm {

    // 视频资源id
    private String videoId;

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

}
