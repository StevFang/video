package com.qs.dao.base;

import com.qs.dto.common.QueryParamDTO;
import com.qs.model.base.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统用户DAO
 *
 * @author FBin
 * @time 2019/1/4 17:07
 */
@Repository
public interface UserDao {

    /**
     * 根据Oid查询 |
     *
     * @param oid 用户oid
     * @return
     */
    User findOne(Long oid);

    /**
     * 根据UserName查询 |
     *
     * @param userName 用户名
     * @return
     */
    List<User> findByUserName(String userName);

    /**
     * 查询全部 |
     *
     * @return
     */
    List<User> findAll();

    /**
     * 根据参数查询 |
     *
     * @param queryParam 查询入参
     * @return
     */
    List<User> findAllByParams(QueryParamDTO queryParam);

}
