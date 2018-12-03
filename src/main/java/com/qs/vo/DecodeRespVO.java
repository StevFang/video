package com.qs.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 解码转码信息
 *
 * Created by fbin on 2018/6/2.
 *
 * @author FBin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DecodeRespVO {

    private String videoId;

    private String decodeLog;

}
