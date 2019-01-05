package com.qs.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 数据表格
 *
 * @author FBin
 * @version 2018/6/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataTableDTO implements Serializable {

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 数据集
     */
    private List dataList;

    /**
     * 当前页码
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pages;

}
