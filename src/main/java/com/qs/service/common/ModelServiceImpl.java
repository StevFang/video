package com.qs.service.common;

import com.alibaba.druid.pool.DruidDataSource;
import com.qs.dto.common.TableDTO;
import com.qs.model.AbstractModel;
import com.qs.service.ModelService;
import com.qs.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author FBin
 * @time 2019/1/7 19:13
 */
@Slf4j
@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public <T> T createObject(Class<T> tClass) {
        try{
            T obj = tClass.newInstance();
            Method method = tClass.getDeclaredMethod("setOid", Long.class);
            method.invoke(obj, CommonUtils.getOid());
            method = tClass.getDeclaredMethod("setParentId", Long.class);
            method.invoke(obj, 0L);
            method = tClass.getDeclaredMethod("setCreatedBy", Long.class);
            method.invoke(obj, SessionUtils.getCurrentUserId(""));
            method = tClass.getDeclaredMethod("setCreatedOn", Date.class);
            method.invoke(obj, DateUtils.now());
            method = tClass.getDeclaredMethod("setCreatedBy", Long.class);
            method.invoke(obj, SessionUtils.getCurrentUserId(""));
            method = tClass.getDeclaredMethod("setUpdatedOn", Date.class);
            method.invoke(obj, DateUtils.now());
            return obj;
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return null;
    }

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
            String saveSql = DataBaseUtils.getSaveSQL(obj, tableDTO);
            return jdbcTemplate.update(saveSql, (preparedStatementSetter) -> DataBaseUtils.setSavePrepareStatement(preparedStatementSetter, obj, tableDTO));
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 更新实例
     *
     * @param obj
     * @return
     */
    @Override
    public Integer update(Object obj){
        try{
            Class<?> clazz = obj.getClass();
            DruidDataSource druidDataSource = (DruidDataSource) jdbcTemplate.getDataSource();
            TableDTO tableDTO = DataBaseUtils.analyzeModelMeta(clazz, druidDataSource);
            String updateSql = DataBaseUtils.getUpdateSQL(obj, tableDTO);
            return jdbcTemplate.update(updateSql, (preparedStatementSetter) -> DataBaseUtils.setUpdatePrepareStatement(preparedStatementSetter, obj, tableDTO));
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 删除实例
     *
     * @param obj
     * @return
     */
    @Override
    public Integer delete(Object obj){
        try{
            Class<?> clazz = obj.getClass();
            DruidDataSource druidDataSource = (DruidDataSource) jdbcTemplate.getDataSource();
            TableDTO tableDTO = DataBaseUtils.analyzeModelMeta(clazz, druidDataSource);
            String deleteSql = DataBaseUtils.getDeleteSQL(obj, tableDTO);
            return jdbcTemplate.update(deleteSql, (preparedStatementSetter) -> DataBaseUtils.setDeletePrepareStatement(preparedStatementSetter, obj, tableDTO));
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return 0;
        }
    }

}
