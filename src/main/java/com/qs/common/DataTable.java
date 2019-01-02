package com.qs.common;

import com.qs.dto.QueryParamDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 数据表格
 *
 * @author FBin
 * @version 2018/6/2.
 */
@Data
@Builder
public class DataTable {

    /**
     * 总记录数
     */
    private long total;

    /**
     * 数据集
     */
    private List<Map<String, Object>> datas;

    /**
     * 当前页码
     */
    private int num;

    /**
     * 每页记录数
     */
    private int rows;

    /**
     * 总页数
     */
    private int pages;

    private DataTable(){

    }

    /**
     * 获取实例
     * @param queryParamDto 查询信息
     * @param total 总数量
     * @param dataList 数据集
     * @return
     */
    public static DataTable getInstance(QueryParamDto queryParamDto,
                                        int total, List<Map<String, Object>> dataList) {
        DataTable dataTable = new DataTable();
        dataTable.setTotal(total);
        dataTable.setDatas(dataList);
        dataTable.setNum(queryParamDto.getNum());
        dataTable.setRows(queryParamDto.getRows());
        dataTable.setPages(calPages(total, queryParamDto.getRows()));

        return dataTable;
    }

    /**
     * 获取分页页数的算法
     * @param total
     * @param rows
     * @return
     */
    private static int calPages(int total, int rows){
        if(total > 0){
            if(total >= rows){
                return Math.floorMod(total, rows) > 0 ?
                        Math.floorDiv(total, rows) :
                        Math.floorDiv(total, rows) + 1;
            }else{
                return 1;
            }
        }else{
            return 0;
        }
    }
}
