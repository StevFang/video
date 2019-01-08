package com.qs.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qs.dto.common.TableDTO;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
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
                            null, "%", tableName, "%");

                    List<Field> declaredFields = ConvertUtil.getClassFields(clazz);
                    Map<String, Field> fieldMap = DataBaseUtils.convertDeclaredFieldsToMap(declaredFields);

                    List<String> primaryPropertyList = Lists.newArrayList();
                    ResultSet primaryKeysSet = databaseMetaData.getPrimaryKeys(null, null, tableName);
                    while (primaryKeysSet.next()){
                        String columnName = primaryKeysSet.getString(DataBaseUtils.COLUMN_NAME);
                        if(fieldMap.containsKey(columnName.toUpperCase())){
                            Field field = fieldMap.get(columnName.toUpperCase());
                            primaryPropertyList.add(field.getName());
                        }
                    }
                    tableDTO.setPrimaryPropertyList(primaryPropertyList);

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

    private static Map<String, Field> convertDeclaredFieldsToMap(List<Field> declaredFields){
        Map<String, Field> fieldMap = Maps.newHashMap();
        for(Field field : declaredFields){
            fieldMap.put(field.getName().toUpperCase(), field);
        }
        return fieldMap;
    }


    /**
     * 获取删除的SQL
     *
     * @param obj
     * @param tableDTO
     * @return
     * @throws Exception
     */
    public static String getDeleteSQL(Object obj, TableDTO tableDTO) throws Exception {
        List<String> primaryFields = tableDTO.getPrimaryPropertyList();

        StringBuilder deleteSql = new StringBuilder();
        String tableName = tableDTO.getTableName();
        deleteSql.append("DELETE FROM ").append(tableName);

        StringBuilder whereSql = new StringBuilder(" WHERE ");
        int initWhereSqlLen = whereSql.length();

        Class<?> clazz = obj.getClass();
        List<Field> declaredFields = ConvertUtil.getClassFields(clazz);
        for(Field field : declaredFields){
            String fieldName = field.getName();
            String columnName = tableDTO.getPropertyColumnMap().get(fieldName);
            if(StringUtils.isNotBlank(columnName)){
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = clazz.getMethod(getMethodName);
                Object fieldValue = method.invoke(obj);
                if(fieldValue != null && primaryFields.contains(fieldName)){
                    if(whereSql.length() > initWhereSqlLen){
                        whereSql.append(" AND ");
                    }
                    whereSql.append(columnName).append("=").append(fieldValue);
                }
            }
        }

        return deleteSql.toString();
    }

    /**
     * 获取更新的SQL
     *
     * @param obj
     * @param tableDTO
     * @return
     * @throws Exception
     */
    public static String getUpdateSQL(Object obj, TableDTO tableDTO) throws Exception {
        List<String> primaryFields = tableDTO.getPrimaryPropertyList();

        StringBuilder updateSql = new StringBuilder();
        String tableName = tableDTO.getTableName();
        updateSql.append("UPDATE ").append(tableName).append(" SET ");

        StringBuilder whereSql = new StringBuilder(" WHERE ");
        int initWhereSqlLen = whereSql.length();

        Class<?> clazz = obj.getClass();
        List<Field> declaredFields = ConvertUtil.getClassFields(clazz);
        for(Field field : declaredFields){
            String fieldName = field.getName();
            String columnName = tableDTO.getPropertyColumnMap().get(fieldName);
            if(StringUtils.isNotBlank(columnName)){
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = clazz.getMethod(getMethodName);
                Object fieldValue = method.invoke(obj);
                if(fieldValue != null){
                    if(primaryFields.contains(fieldName)){
                        if(whereSql.length() > initWhereSqlLen){
                            whereSql.append(" AND ");
                        }
                        whereSql.append(columnName).append("=?");
                    }else {
                        updateSql.append(columnName).append("=?").append(", ");
                    }
                }
            }
        }

        return updateSql.toString();
    }

    /**
     * 获取保存的SQL
     *
     * @param obj
     * @param tableDTO
     * @return
     * @throws Exception
     */
    public static String getSaveSQL(Object obj, TableDTO tableDTO) throws Exception{
        StringBuilder saveSql = new StringBuilder();
        String tableName = tableDTO.getTableName();
        saveSql.append("INSERT TABLE ").append(tableName);

        StringBuilder columnSql = new StringBuilder();
        StringBuilder prepareStatementSql = new StringBuilder();

        Class<?> clazz = obj.getClass();
        List<Field> declaredFields = ConvertUtil.getClassFields(clazz);
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

    /**
     * 设置删除实例的包含已编译的SQL对象
     *
     * @param preparedStatement
     * @param obj
     * @param tableDTO
     */
    public static void setDeletePrepareStatement(PreparedStatement preparedStatement, Object obj, TableDTO tableDTO) {
        try {
            List<String> primaryFields = tableDTO.getPrimaryPropertyList();

            Class<?> clazz = obj.getClass();
            List<Field> declaredFields = ConvertUtil.getClassFields(clazz);

            int parameterIndex = 1;

            for(Field field : declaredFields){
                String fieldName = field.getName();
                String columnName = tableDTO.getPropertyColumnMap().get(fieldName);
                if(StringUtils.isNotBlank(columnName)){
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = clazz.getMethod(getMethodName);
                    Object fieldValue = method.invoke(obj);
                    if(fieldValue != null && primaryFields.contains(fieldName)){
                        preparedStatement.setObject(parameterIndex, fieldValue);
                        parameterIndex++;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设置更新实例的包含已编译的SQL对象
     *
     * @param preparedStatement
     * @param obj
     * @param tableDTO
     */
    public static void setUpdatePrepareStatement(PreparedStatement preparedStatement, Object obj,  TableDTO tableDTO) {
        try {
            List<String> primaryFields = tableDTO.getPrimaryPropertyList();

            Class<?> clazz = obj.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();

            int parameterIndex = 1;
            List<Object> whereParameterList = Lists.newArrayList();

            for(Field field : declaredFields){
                String fieldName = field.getName();
                String columnName = tableDTO.getPropertyColumnMap().get(fieldName);
                if(StringUtils.isNotBlank(columnName)){
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = clazz.getMethod(getMethodName);
                    Object fieldValue = method.invoke(obj);
                    if(fieldValue != null){
                        if(primaryFields.contains(fieldName)){
                            whereParameterList.add(fieldValue);
                        }else{
                            preparedStatement.setObject(parameterIndex, fieldValue);
                        }
                        parameterIndex++;
                    }
                }
            }

            for(Object object : whereParameterList){
                preparedStatement.setObject(parameterIndex, object);
                parameterIndex++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设置保存实例的包含已编译的SQL对象
     *
     * @param preparedStatement
     * @param obj
     * @param tableDTO
     */
    public static void setSavePrepareStatement(PreparedStatement preparedStatement, Object obj, TableDTO tableDTO) {
        try {
            Class<?> clazz = obj.getClass();
            List<Field> declaredFields = ConvertUtil.getClassFields(clazz);
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
