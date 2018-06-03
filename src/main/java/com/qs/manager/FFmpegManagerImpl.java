package com.qs.manager;

import com.qs.config.FFmpegConfig;
import com.qs.dao.TaskDao;
import com.qs.entity.TaskEntity;
import com.qs.service.CommandService;
import com.qs.service.TaskHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;



/**
 * FFmpeg命令操作管理器
 * 
 * @author fbin
 */
@Component("ffmpegManager")
public class FFmpegManagerImpl implements FFmpegManager {

	/**
	 * 记录日志
	 */
	private static Logger logger = LoggerFactory.getLogger(FFmpegManagerImpl.class);

	/**
	 * 任务持久化器
	 */
	@Autowired
	private TaskDao taskDao;

	/**
	 * 任务执行处理器
	 */
	@Autowired
	private TaskHandler taskHandler;

	/**
	 * 创建ffmpeg指令
	 */
	@Autowired
	private CommandService ffmpegCommandService;

	private FFmpegConfig ffmpegConfig = null;

	@Override
	public String start(String id, String command) {
		return start(id, command, false);
	}

	@Override
	public String start(String id, String command, boolean hasPath) {
		if (id != null && command != null) {
			TaskEntity task = taskHandler.process(id, hasPath ? command : ffmpegConfig.getFfmpegPath() + command);
			if (task != null) {
				int ret = taskDao.add(task);
				if (ret > 0) {
					return task.getId();
				} else {
					// 持久化信息失败，停止处理
					taskHandler.stop(task.getProcess(), task.getThread());
				}
			}
		}
		return null;
	}

	@Override
	public String start(FFmpegConfig ffmpegConfig, String id) {

		this.ffmpegConfig = ffmpegConfig;

		String commandLine = ffmpegCommandService.createCommand(ffmpegConfig);

		if (StringUtils.isNotBlank(commandLine)) {
			return start(id, commandLine, true);
		}

		return null;
	}

	@Override
	public boolean stop(String id) {
		if (id != null && taskDao.isHave(id)) {
			TaskEntity task = taskDao.get(id);
			if (taskHandler.stop(task.getProcess(), task.getThread())) {
				taskDao.remove(id);
				return true;
			}
		}
		logger.error("停止任务失败！id=" + id);
		return false;
	}

	@Override
	public int stopAll() {
		Collection<TaskEntity> list = taskDao.getAll();
		Iterator<TaskEntity> iter = list.iterator();
		TaskEntity tasker = null;
		int index = 0;
		while (iter.hasNext()) {
			tasker = iter.next();
			if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
				taskDao.remove(tasker.getId());
				index++;
			}
		}
		return index;
	}

	@Override
	public TaskEntity query(String id) {
		return taskDao.get(id);
	}

	@Override
	public Collection<TaskEntity> queryAll() {
		return taskDao.getAll();
	}
}
