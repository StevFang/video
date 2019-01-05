package com.qs.dao;

import com.qs.Application;
import com.qs.dao.base.SysUserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author FBin
 * @time 2019/1/4 17:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})
@Slf4j
public class SysUserDaoTest {

    @Autowired
    private SysUserDao sysUserDao;

    @Test
    public void findOne(){
        System.out.println(sysUserDao.findAll());
    }

}
