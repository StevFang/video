package com.qs.dto.common;

import lombok.Data;

/**
 * 查询参数信息
 *
 * Created by fbin on 2018/6/2.
 *
 * @author FBin
 */
@Data
public class QueryParamDTO {

    protected Integer current;

    protected Integer pageSize;

    protected String[] queryFields;

    protected String[] queryValues;

    protected String[] sortFields;

    protected String[] sortValues;

}
