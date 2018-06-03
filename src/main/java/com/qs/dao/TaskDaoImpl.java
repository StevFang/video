package com.qs.dao;

import com.qs.model.TaskModel;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 任务信息持久层实现
 *
 * @author fbin
 */
@Component("taskDao")
public class TaskDaoImpl implements TaskDao {

    // 存放任务信息
    private ConcurrentMap<String, TaskModel> map = new ConcurrentHashMap<>();

    @Override
    public TaskModel get(String id) {
        return map.get(id);
    }

    @Override
    public Collection<TaskModel> getAll() {
        return map.values();
    }

    @Override
    public int add(TaskModel taskModel) {
        String id = taskModel.getId();
        if (id != null && !map.containsKey(id)) {
            map.put(taskModel.getId(), taskModel);
            if (map.get(id) != null) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int remove(String id) {
        if (map.remove(id) != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public int removeAll() {
        int size = map.size();
        try {
            map.clear();
        } catch (Exception e) {
            return 0;
        }
        return size;
    }

    @Override
    public boolean isHave(String id) {
        return map.containsKey(id);
    }

}
