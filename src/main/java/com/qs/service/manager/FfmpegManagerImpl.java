package com.qs.service.manager;

import com.qs.service.TaskService;
import com.qs.dto.config.BaseffmpegDTO;
import com.qs.dto.config.DecodeffmpegDTO;
import com.qs.dto.config.LiveffmpegDTO;
import com.qs.dto.video.TaskDTO;
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
	private TaskService taskService;

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

	private LiveffmpegDTO liveDTO = null;

	private DecodeffmpegDTO decodeDTO = null;

	@Override
	public String start(String appName, String command) {
		return start(appName, command, false);
	}

	@Override
	public String start(String appName, String command, boolean hasPath) {
		log.info("command= "+ command);
		if (appName != null && command != null) {
			TaskDTO task = taskHandler.process(appName, hasPath ? command : liveDTO.getFfmpegPath() + command);
			if (task != null) {
				int ret = taskService.add(task);
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
	public String start(BaseffmpegDTO baseffmpegDTO) {

		String commandLine = null;

		if(baseffmpegDTO instanceof LiveffmpegDTO){
			// 直播配置
			this.liveDTO = (LiveffmpegDTO) baseffmpegDTO;
			commandLine = ffmpegOnlineCommandService.createCommand(liveDTO);
			if (StringUtils.isNotBlank(commandLine)) {
				return start(liveDTO.getAppName(), commandLine, true);
			}
		}else if(baseffmpegDTO instanceof DecodeffmpegDTO){
			// 转码配置
			this.decodeDTO = (DecodeffmpegDTO) baseffmpegDTO;
			commandLine = ffmpegDecodeCommandService.createCommand(decodeDTO);
			if (StringUtils.isNotBlank(commandLine)) {
				return start(decodeDTO.getAppName(), commandLine, true);
			}
		}

		return null;
	}

	@Override
	public boolean stop(String id) {
		if (id != null && taskService.isHave(id)) {
			TaskDTO task = taskService.get(id);
			if (taskHandler.stop(task.getProcess(), task.getThread())) {
				taskService.remove(id);
				return true;
			}
		}
		log.error("停止任务失败！id=" + id);
		return false;
	}

	@Override
	public int stopAll() {
		Collection<TaskDTO> list = taskService.getAll();
		Iterator<TaskDTO> iter = list.iterator();
		TaskDTO tasker = null;
		int index = 0;
		while (iter.hasNext()) {
			tasker = iter.next();
			if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
				taskService.remove(tasker.getId());
				index++;
			}
		}
		return index;
	}

	@Override
	public TaskDTO query(String id) {
		return taskService.get(id);
	}

	@Override
	public Collection<TaskDTO> queryAll() {
		return taskService.getAll();
	}
}
