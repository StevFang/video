package com.qs.dao.base;

import com.qs.dto.common.QueryParamDTO;
import com.qs.model.base.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统用户DAO
 *
 * @author FBin
 * @time 2019/1/4 17:07
 */
@Repository
public interface SysUserDao {

    /**
     * 根据Oid查询 |
     *
     * @param oid
     * @return
     */
    SysUser findOne(Long oid);

    /**
     * 根据UserName查询 |
     *
     * @param userName
     * @return
     */
    List<SysUser> findByUserName(String userName);

    /**
     * 查询全部 |
     *
     * @return
     */
    List<SysUser> findAll();

    /**
     * 根据参数查询 |
     *
     * @param queryParam
     * @return
     */
    List<SysUser> findAllByParams(QueryParamDTO queryParam);

}
