package com.qs.service;

/**
 * 命令生成接口
 * @param <T>
 */
public interface CommandService<T> {

    String createCommand(T t);

}
