package com.qs.service;

/**
 * @author FBin
 * @time 2019/1/7 21:39
 */
public interface ModelService {

    /**
     * 保存对象实例
     *
     * @param obj
     * @return
     */
    Integer save(Object obj);

    /**
     * 更新对象实例
     *
     * @param obj
     * @return
     */
    Integer update(Object obj);

    /**
     * 删除对象实例
     *
     * @param obj
     * @return
     */
    Integer delete(Object obj);
}
