package com.qs.service.manager;

import com.qs.dao.TaskDao;
import com.qs.dto.config.BaseFastForwardMovingPictureExpertsGroupConfig;
import com.qs.dto.config.FastForwardMovingPictureExpertsGroupDecodeConfig;
import com.qs.dto.config.FastForwardMovingPictureExpertsGroupLiveConfig;
import com.qs.model.TaskModel;
import com.qs.service.CommandService;
import com.qs.service.FfmpegManager;
import com.qs.service.handler.TaskHandlerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;



/**
 * FFmpeg命令操作管理器
 * 
 * @author fbin
 */
@Slf4j
@Service
public class FfmpegManagerImpl implements FfmpegManager {

	/**
	 * 任务持久化器
	 */
	@Autowired
	private TaskDao taskDao;

	/**
	 * 任务执行处理器
	 */
	@Autowired
	private TaskHandlerImpl taskHandler;

	/**
	 * ffmpeg 直播推流服务
	 */
	@Resource(name = "ffmpegOnlineCommandService")
	private CommandService ffmpegOnlineCommandService;

	/**
	 * ffmpeg 视频转码服务
	 */
	@Resource(name = "ffmpegDecodeCommandService")
	private CommandService ffmpegDecodeCommandService;

	private FastForwardMovingPictureExpertsGroupLiveConfig fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig = null;

	private FastForwardMovingPictureExpertsGroupDecodeConfig fastForwardMovingPictureExpertsGroupDecodeConfigConfig = null;

	@Override
	public String start(String appName, String command) {
		return start(appName, command, false);
	}

	@Override
	public String start(String appName, String command, boolean hasPath) {
		log.info("command= "+ command);
		if (appName != null && command != null) {
			TaskModel task = taskHandler.process(appName, hasPath ? command : fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig.getFfmpegPath() + command);
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
	public String start(BaseFastForwardMovingPictureExpertsGroupConfig baseFastForwardMovingPictureExpertsGroupConfig) {

		String commandLine = null;

		if(baseFastForwardMovingPictureExpertsGroupConfig instanceof FastForwardMovingPictureExpertsGroupLiveConfig){
			// 直播配置
			this.fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig = (FastForwardMovingPictureExpertsGroupLiveConfig) baseFastForwardMovingPictureExpertsGroupConfig;
			commandLine = ffmpegOnlineCommandService.createCommand(fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig);
			if (StringUtils.isNotBlank(commandLine)) {
				return start(fastForwardMovingPictureExpertsGroupLiveConfigDTOConfig.getAppName(), commandLine, true);
			}
		}else if(baseFastForwardMovingPictureExpertsGroupConfig instanceof FastForwardMovingPictureExpertsGroupDecodeConfig){
			// 转码配置
			this.fastForwardMovingPictureExpertsGroupDecodeConfigConfig = (FastForwardMovingPictureExpertsGroupDecodeConfig) baseFastForwardMovingPictureExpertsGroupConfig;
			commandLine = ffmpegDecodeCommandService.createCommand(fastForwardMovingPictureExpertsGroupDecodeConfigConfig);
			if (StringUtils.isNotBlank(commandLine)) {
				return start(fastForwardMovingPictureExpertsGroupDecodeConfigConfig.getAppName(), commandLine, true);
			}
		}

		return null;
	}

	@Override
	public boolean stop(String id) {
		if (id != null && taskDao.isHave(id)) {
			TaskModel task = taskDao.get(id);
			if (taskHandler.stop(task.getProcess(), task.getThread())) {
				taskDao.remove(id);
				return true;
			}
		}
		log.error("停止任务失败！id=" + id);
		return false;
	}

	@Override
	public int stopAll() {
		Collection<TaskModel> list = taskDao.getAll();
		Iterator<TaskModel> iter = list.iterator();
		TaskModel tasker = null;
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
	public TaskModel query(String id) {
		return taskDao.get(id);
	}

	@Override
	public Collection<TaskModel> queryAll() {
		return taskDao.getAll();
	}
}
