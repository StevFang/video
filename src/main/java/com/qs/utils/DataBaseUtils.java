package com.qs.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps;
import com.qs.dto.common.TableDTO;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Map;

/**
 * @author FBin
 * @time 2019/1/7 20:02
 */
public class DataBaseUtils {

    /**
     * 类型
     */
    public static final String TYPE_OF_TABLE = "TABLE";

    /**
     * 表名
     */
    public static final String TABLE_NAME = "TABLE_NAME";

    /**
     * 表类型
     */
    public static final String TABLE_TYPE = "TABLE_TYPE";

    /**
     * 表所属数据库
     */
    public static final String TABLE_CAT = "TABLE_CAT";

    /**
     * 表 | 字段 备注
     */
    public static final String REMARKS = "REMARKS";

    /**
     * 字段名
     */
    public static final String COLUMN_NAME = "COLUMN_NAME";

    /**
     * 字段类型
     */
    public static final String COLUMN_TYPE_NAME = "TYPE_NAME";

    /**
     * 字段大小
     */
    public static final String COLUMN_SIZE = "COLUMN_SIZE";


    private static Map<String, TableDTO> tableDTOMap = Maps.newConcurrentMap();

    /**
     * 解析实体的元数据
     *
     * @param clazz
     * @param druidDataSource
     * @return
     * @throws Exception
     */
    public static TableDTO analyzeModelMeta(Class<?> clazz, DruidDataSource druidDataSource) throws Exception {
        // 表名
        String clazzSimpleName = clazz.getSimpleName();
        if(tableDTOMap.containsKey(clazzSimpleName.toUpperCase())){
            return tableDTOMap.get(clazzSimpleName.toUpperCase());
        }else{
            TableDTO tableDTO = TableDTO.builder().build();
            Connection connection = druidDataSource.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();

            ResultSet databaseResultSet = databaseMetaData.getTables(
                    null, null, null, new String[] { DataBaseUtils.TYPE_OF_TABLE });
            while(databaseResultSet.next()){
                String tableName = databaseResultSet.getString(DataBaseUtils.TABLE_NAME);
                tableDTO.setTableName(tableName);

                if(clazzSimpleName.toUpperCase().equals(tableName.toUpperCase())) {
                    ResultSet rsColumns = databaseMetaData.getColumns(
                            null, "%", databaseResultSet.getString(DataBaseUtils.TABLE_NAME), "%");

                    Field[] declaredFields = clazz.getDeclaredFields();
                    Map<String, Field> fieldMap = DataBaseUtils.convertDeclaredFieldsToMap(declaredFields);

                    Map<String, String> propertyColumnMap = Maps.newHashMap();
                    Map<String, String> columnTypeMap = Maps.newHashMap();
                    Map<String, Class<?>> propertyTypeMap = Maps.newHashMap();

                    while (rsColumns.next()) {
                        String columnName = rsColumns.getString(DataBaseUtils.COLUMN_NAME);
                        String columnTypeName = rsColumns.getString(DataBaseUtils.COLUMN_TYPE_NAME);
                        if(fieldMap.containsKey(columnName.toUpperCase())){
                            Field field = fieldMap.get(columnName.toUpperCase());
                            propertyColumnMap.put(field.getName(), columnName);
                            columnTypeMap.put(columnName, columnTypeName);
                            propertyTypeMap.put(field.getName(), field.getType());
                        }
                    }
                    tableDTO.setPropertyColumnMap(propertyColumnMap);
                    tableDTO.setColumnTypeMap(columnTypeMap);
                    tableDTO.setPropertyTypeMap(propertyTypeMap);
                    break;
                }
            }
            return tableDTO;
        }
    }

    private static Map<String, Field> convertDeclaredFieldsToMap(Field[] declaredFields){
        Map<String, Field> fieldMap = Maps.newHashMap();
        for(Field field : declaredFields){
            fieldMap.put(field.getName().toUpperCase(), field);
        }
        return fieldMap;
    }
}
