package com.qs.dto.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询参数信息
 *
 * Created by fbin on 2018/6/2.
 *
 * @author FBin
 */
@Data
public class QueryParamDTO implements Serializable {

    private Integer current;

    private Integer pageSize;

    private String[] queryFields;

    private String[] queryValues;

    private String[] sortFields;

    private String[] sortValues;

}
