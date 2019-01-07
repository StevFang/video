package com.qs.service;

import com.qs.dto.video.TaskDTO;

import java.util.Collection;


/**
 * 任务信息持久层接口
 * @author fbin
 */
public interface TaskService {
	/**
	 * 通过id查询任务信息
	 * @param id - 任务ID
	 * @return TaskModel -任务实体
	 */
	TaskDTO get(String id);

	/**
	 * 查询全部任务信息
	 * @return Collection<TaskModel>
	 */
	Collection<TaskDTO> getAll();

	/**
	 * 增加任务信息
	 * @param taskDTO -任务信息实体
	 * @return 增加数量：<1-增加失败，>=1-增加成功
	 */
	int add(TaskDTO taskDTO);

	/**
	 * 删除id对应的任务信息
	 * @param id
	 * @return 数量：<1-操作失败，>=1-操作成功
	 */
	int remove(String id);

	/**
	 * 删除全部任务信息
	 * @return 数量：<1-操作失败，>=1-操作成功
	 */
	int removeAll();

	/**
	 * 是否存在某个ID
	 * @param id - 任务ID
	 * @return true:存在，false：不存在
	 */
	boolean isHave(String id);
}
