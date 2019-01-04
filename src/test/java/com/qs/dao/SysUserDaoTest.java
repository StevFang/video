package com.qs.dao;

import com.qs.Application;
import com.qs.dao.base.SysUserDao;
import com.qs.model.base.context.VideoContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author FBin
 * @time 2019/1/4 17:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})// 指定启动类
@Slf4j
public class SysUserDaoTest {

    @Test
    public void findOne(){
        SysUserDao sysUserDao = (SysUserDao) VideoContext.getBean("sysUserDao");
    }

}
