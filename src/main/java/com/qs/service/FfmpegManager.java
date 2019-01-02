package com.qs.service;

import com.qs.dto.config.BaseFastForwardMovingPictureExpertsGroupConfig;
import com.qs.model.TaskModel;

import java.util.Collection;

/**
 * FFmpeg命令操作管理器，可执行FFmpeg命令/停止/查询任务信息
 * 
 * @author fbin
 */
public interface FfmpegManager {

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
	 * @param command - FFmpeg命令
	 * @param hasPath - 命令中是否包含FFmpeg执行文件的绝对路径
	 * @return
	 */
	String start(String id, String command, boolean hasPath);

	/**
	 * 通过组装命令发布任务
	 * 
	 * @param baseFastForwardMovingPictureExpertsGroupConfig -组装命令所需配置
	 *
	 * @return
	 */
	String start(BaseFastForwardMovingPictureExpertsGroupConfig baseFastForwardMovingPictureExpertsGroupConfig);
	
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
	TaskModel query(String id);

	/**
	 * 查询全部任务信息
	 * 
	 */
	Collection<TaskModel> queryAll();
}
