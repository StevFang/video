package com.qs.manager;

import com.qs.config.FFmpegConfig;
import com.qs.entity.TaskEntity;

import java.util.Collection;

/**
 * FFmpeg命令操作管理器，可执行FFmpeg命令/停止/查询任务信息
 * 
 * @author fbin
 */
public interface FFmpegManager {

	/**
	 * 通过命令发布任务（默认命令前不加FFmpeg路径）
	 * 
	 * @param id - 任务标识
	 * @param command - FFmpeg命令
	 * @return
	 */
	String start(String id, String command);

	/**
	 * 通过命令发布任务
	 * @param id - 任务标识
	 * @param commond - FFmpeg命令
	 * @param hasPath - 命令中是否包含FFmpeg执行文件的绝对路径
	 * @return
	 */
	String start(String id, String commond, boolean hasPath);

	/**
	 * 通过组装命令发布任务
	 * 
	 * @param ffmpegConfig -组装命令所需配置
	 *
	 * @return
	 */
	String start(FFmpegConfig ffmpegConfig, String id);
	
	/**
	 * 停止任务
	 * 
	 * @param id
	 * @return
	 */
	boolean stop(String id);

	/**
	 * 停止全部任务
	 * 
	 * @return
	 */
	int stopAll();

	/**
	 * 通过id查询任务信息
	 * 
	 * @param id
	 */
	TaskEntity query(String id);

	/**
	 * 查询全部任务信息
	 * 
	 */
	Collection<TaskEntity> queryAll();
}
