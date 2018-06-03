package com.qs.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 解码转码信息
 *
 * Created by fbin on 2018/6/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DecodeInfo {

    private String videoId;

    private String decodeLog;

}
