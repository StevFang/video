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

    protected long num = 0;

    protected long rows = 0;

    protected String[] sorts;

    protected long getStart(){
        return rows * (num - 1);
    }

}
