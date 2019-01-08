package com.qs.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.qs.Application;
import com.qs.dao.base.UserDao;
import com.qs.dto.common.TableDTO;
import com.qs.model.upload.UploadRecord;
import com.qs.service.ModelService;
import com.qs.utils.ConvertUtil;
import com.qs.utils.DataBaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author FBin
 * @time 2019/1/4 17:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})
@Slf4j
public class DaoTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ModelService modelService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void findOne(){
        System.out.println(userDao.findAll());
    }

    @Test
    public void analyzeDataSource() throws Exception {
        UploadRecord uploadRecord = UploadRecord.builder().build();
        uploadRecord.setOid(100000L);
        uploadRecord.setParentId(0L);
        uploadRecord.setCreatedOn(new Date());
        uploadRecord.setUpdatedOn(new Date());
        // uploadRecord.setCreatedBy(1L);
        // uploadRecord.setUpdatedBy(1L);
        uploadRecord.setCode("UP2019010800001");
        uploadRecord.setOriginName("小.png");
        uploadRecord.setSaveName(ConvertUtil.getFormatUUID());
        uploadRecord.setExtName("png");
        TableDTO updateRecordTableDto = DataBaseUtils.analyzeModelMeta(UploadRecord.class, (DruidDataSource) jdbcTemplate.getDataSource());

        String insertSql = DataBaseUtils.getSaveSQL(uploadRecord, updateRecordTableDto);
        String updateSql = DataBaseUtils.getUpdateSQL(uploadRecord, updateRecordTableDto);
        String deleteSql = DataBaseUtils.getDeleteSQL(uploadRecord, updateRecordTableDto);

        System.out.println("新增：" + insertSql);
        System.out.println("编辑：" + updateSql);
        System.out.println("删除：" + deleteSql);
    }

    @Test
    public void saveUploadRecord(){
        UploadRecord uploadRecord = UploadRecord.builder().build();
        uploadRecord.setOid(100001L);
        uploadRecord.setParentId(0L);
        uploadRecord.setCreatedOn(new Date());
        uploadRecord.setUpdatedOn(new Date());
        uploadRecord.setCreatedBy(1L);
        uploadRecord.setUpdatedBy(1L);
        uploadRecord.setCode("UP2019010800001");
        uploadRecord.setOriginName("小.png");
        uploadRecord.setSaveName(ConvertUtil.getFormatUUID()+".png");
        uploadRecord.setExtName("png");

        System.out.println(modelService.save(uploadRecord));
    }

}
