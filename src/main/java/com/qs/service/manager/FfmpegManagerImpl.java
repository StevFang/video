package com.qs.service.manager;

import com.qs.dao.TaskDao;
import com.qs.dto.config.BaseFastForwardMovingPictureExpertsGroupDTO;
import com.qs.dto.config.FastForwardMovingPictureExpertsGroupDecodeDTO;
import com.qs.dto.config.FastForwardMovingPictureExpertsGroupLiveDTO;
import com.qs.model.TaskModel;
import com.qs.service.FfmpegManager;
import com.qs.service.command.ffmpeg.FfmpegDecodeCommandServiceImpl;
import com.qs.service.command.ffmpeg.FfmpegOnlineCommandServiceImpl;
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
	@Resource
	private FfmpegOnlineCommandServiceImpl ffmpegOnlineCommandService;

	/**
	 * ffmpeg 视频转码服务
	 */
	@Resource
	private FfmpegDecodeCommandServiceImpl ffmpegDecodeCommandService;

	private FastForwardMovingPictureExpertsGroupLiveDTO liveDTO = null;

	private FastForwardMovingPictureExpertsGroupDecodeDTO decodeDTO = null;

	@Override
	public String start(String appName, String command) {
		return start(appName, command, false);
	}

	@Override
	public String start(String appName, String command, boolean hasPath) {
		log.info("command= "+ command);
		if (appName != null && command != null) {
			TaskModel task = taskHandler.process(appName, hasPath ? command : liveDTO.getFfmpegPath() + command);
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
	public String start(BaseFastForwardMovingPictureExpertsGroupDTO baseFastForwardMovingPictureExpertsGroupDTO) {

		String commandLine = null;

		if(baseFastForwardMovingPictureExpertsGroupDTO instanceof FastForwardMovingPictureExpertsGroupLiveDTO){
			// 直播配置
			this.liveDTO = (FastForwardMovingPictureExpertsGroupLiveDTO) baseFastForwardMovingPictureExpertsGroupDTO;
			commandLine = ffmpegOnlineCommandService.createCommand(liveDTO);
			if (StringUtils.isNotBlank(commandLine)) {
				return start(liveDTO.getAppName(), commandLine, true);
			}
		}else if(baseFastForwardMovingPictureExpertsGroupDTO instanceof FastForwardMovingPictureExpertsGroupDecodeDTO){
			// 转码配置
			this.decodeDTO = (FastForwardMovingPictureExpertsGroupDecodeDTO) baseFastForwardMovingPictureExpertsGroupDTO;
			commandLine = ffmpegDecodeCommandService.createCommand(decodeDTO);
			if (StringUtils.isNotBlank(commandLine)) {
				return start(decodeDTO.getAppName(), commandLine, true);
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
