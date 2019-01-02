package com.qs.dto;

import lombok.Data;

/**
 * 查询参数信息
 *
 * Created by fbin on 2018/6/2.
 *
 * @author FBin
 */
@Data
public class QueryParamDto {

    protected int num = 0;

    protected int rows = 0;

    protected String[] sorts;

    protected int getStart(){
        return rows * (num - 1);
    }

}
