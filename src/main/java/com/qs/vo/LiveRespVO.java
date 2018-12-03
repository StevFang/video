package com.qs.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推流返回信息
 *
 * @author FBin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LiveRespVO {

    private String output;

    private String message;

}
