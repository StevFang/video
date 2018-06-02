package com.qs.form;

import lombok.Data;

/**
 * 查询参数信息
 *
 * Created by fbin on 2018/6/2.
 */
@Data
public class QueryInfo {

    protected long num = 0;

    protected long rows = 0;

    protected String[] sorts;

    protected long getStart(){
        return rows * (num - 1);
    }

}
