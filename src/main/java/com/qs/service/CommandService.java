package com.qs.service;

/**
 * 命令生成接口
 *
 * @author FBin
 */
public interface CommandService<T> {

    /**
     * 创建命令行命令
     *
     * @param t
     * @return
     */
    String createCommand(T t);

}
