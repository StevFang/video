package com.qs.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页信息
 *
 * Created by fbin on 2018/6/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageInfo {

    private Long num;

    private Long rows;

    private String[] sorts;

    public Long getStart(){
        return rows * (num - 1);
    }

}
