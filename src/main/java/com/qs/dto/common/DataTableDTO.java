package com.qs.dto.common;

import com.qs.utils.CommonUtils;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 数据表格
 *
 * @author FBin
 * @version 2018/6/2.
 */
@Data
public class DataTableDTO<T> {

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 数据集
     */
    private List<T> dataList;

    /**
     * 当前页码
     */
    private Integer num;

    /**
     * 每页记录数
     */
    private Integer rows;

    /**
     * 总页数
     */
    private Integer pages;

    private DataTableDTO(){

    }

    /**
     * 获取实例
     * @param queryParamDTO 查询信息
     * @param total 总数量
     * @param dataList 数据集
     * @return
     */
    public static <T> DataTableDTO getInstance(QueryParamDTO queryParamDTO,
                                           int total, List<T> dataList) {
        DataTableDTO<T> dataTableDTO = new DataTableDTO<>();
        dataTableDTO.setTotal(total);
        dataTableDTO.setDataList(dataList);
        dataTableDTO.setNum(queryParamDTO.getCurrent());
        dataTableDTO.setRows(queryParamDTO.getPageSize());
        dataTableDTO.setPages(CommonUtils.calPages(total, queryParamDTO.getPageSize()));

        return dataTableDTO;
    }

}
