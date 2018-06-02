package com.qs.form;

import lombok.Builder;
import lombok.Data;

/**
 * 解码转码信息
 *
 * Created by fbin on 2018/6/2.
 */
@Data
@Builder
public class DecodeInfo {

    private String videoId;

    // 目标编码
    private String targetCode;

    // 是否查看进度
    private String showProgress;

    // 是否增加水印


}
