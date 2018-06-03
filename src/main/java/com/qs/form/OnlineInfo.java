package com.qs.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推流返回信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnlineInfo {

    private String output;

    private String message;

}
