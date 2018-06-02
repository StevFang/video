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

    private String videoId;

    // 目标编码
    private String targetCode;

    // 是否查看进度
    private String showProgress;

    // 是否增加水印

}
