package com.qs.dao;

import com.qs.Application;
import com.qs.dao.base.UserDao;
import com.qs.model.upload.UploadRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    private com.qs.service.common.ModelServiceImpl ModelServiceImpl;

    @Test
    public void findOne(){
        System.out.println(userDao.findAll());
    }

    @Test
    public void analyzeDataSource() throws Exception {
        // ModelServiceImpl.analyzeModelMeta(UploadRecord.class);
    }

}
