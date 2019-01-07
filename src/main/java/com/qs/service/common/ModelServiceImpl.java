package com.qs.service.common;

import com.alibaba.druid.pool.DruidDataSource;
import com.qs.dto.common.TableDTO;
import com.qs.service.ModelService;
import com.qs.utils.DataBaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;

/**
 * @author FBin
 * @time 2019/1/7 19:13
 */
@Slf4j
@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存实例
     *
     * @param obj
     * @return
     * @throws Exception
     */
    @Override
    public Integer save(Object obj) {
        try {
            Class<?> clazz = obj.getClass();
            DruidDataSource druidDataSource = (DruidDataSource) jdbcTemplate.getDataSource();
            TableDTO tableDTO = DataBaseUtils.analyzeModelMeta(clazz, druidDataSource);
            String saveSql = this.getSaveSQL(obj, tableDTO);
            return jdbcTemplate.update(saveSql, (preparedStatementSetter) -> this.setSavePrepareStatement(preparedStatementSetter, obj, tableDTO));
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    private String getSaveSQL(Object obj, TableDTO tableDTO) throws Exception{
        StringBuilder saveSql = new StringBuilder();
        String tableName = tableDTO.getTableName();
        saveSql.append("INSERT TABLE ").append(tableName);

        StringBuilder columnSql = new StringBuilder();
        StringBuilder prepareStatementSql = new StringBuilder();

        Class<?> clazz = obj.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field field : declaredFields){
            String fieldName = field.getName();
            String columnName = tableDTO.getPropertyColumnMap().get(fieldName);
            if(StringUtils.isNotBlank(columnName)){
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = clazz.getMethod(getMethodName);
                Object fieldValue = method.invoke(obj);
                if(fieldValue != null){
                    columnSql.append(columnName).append(", ");
                    prepareStatementSql.append("?").append(", ");
                }
            }
        }

        if(columnSql.length() > 0){
            int columnSqlLength = columnSql.length();
            saveSql.append("(").append(columnSql.substring(0, columnSqlLength - 2)).append(")");

            int prepareStatementSqlLength = prepareStatementSql.length();
            saveSql.append("VALUES(").append(prepareStatementSql.substring(0, prepareStatementSqlLength - 2)).append(")");
        }
        return saveSql.toString();
    }

    private void setSavePrepareStatement(PreparedStatement preparedStatement, Object obj, TableDTO tableDTO) {
        try {
            Class<?> clazz = obj.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            int parameterIndex = 1;
            for(Field field : declaredFields){
                String fieldName = field.getName();
                String columnName = tableDTO.getPropertyColumnMap().get(fieldName);
                if(StringUtils.isNotBlank(columnName)){
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = clazz.getMethod(getMethodName);
                    Object fieldValue = method.invoke(obj);
                    if(fieldValue != null){
                        preparedStatement.setObject(parameterIndex, fieldValue);
                        parameterIndex++;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
