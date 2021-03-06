package com.qs.service;

import com.qs.dto.video.TaskDTO;

/**
 * 任务执行接口
 * @author fbin
 */
public interface TaskHandler {
	/**
	 * 按照命令执行主进程和输出线程
	 * 
	 * @param appName
	 * @param command
	 * @return
	 */
	TaskDTO process(String appName, String command);

	/**
	 * 停止主进程（停止主进程需要保证输出线程已经关闭，否则输出线程会出错）
	 * 
	 * @param process
	 * @return
	 */
	boolean stop(Process process);

	/**
	 * 停止输出线程
	 * 
	 * @param thread
	 * @return
	 */
	boolean stop(Thread thread);

	/**
	 * 正确的停止输出线程和主进程
	 * 
	 * @param process
	 * @param thread
	 * @return
	 */
	boolean stop(Process process, Thread thread);
}
