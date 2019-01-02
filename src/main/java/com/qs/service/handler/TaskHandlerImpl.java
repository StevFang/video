package com.qs.service.handler;

import com.qs.model.TaskModel;
import com.qs.service.TaskHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 任务处理实现
 *
 * @author fbin
 */
@Service
public class TaskHandlerImpl implements TaskHandler {

    private Runtime runtime = null;

    /**
     * 任务消息处理器
     */
    @Resource(name = "defaultOutHandlerMethod")
    private DefaultOutHandlerMethod defaultOutHandlerMethod;

    @Override
    public TaskModel process(String appName, String command) {
        Process process = null;
        TaskMessageOutHandler taskMessageOutHandler = null;
        TaskModel taskModel = null;
        try {
            if (runtime == null) {
                runtime = Runtime.getRuntime();
            }
            // 执行本地命令获取任务主进程
            process = runtime.exec(command);
            taskMessageOutHandler = new TaskMessageOutHandler(process.getErrorStream(), appName, defaultOutHandlerMethod);
            taskMessageOutHandler.start();
            taskModel = new TaskModel(appName, process, taskMessageOutHandler);
        } catch (IOException e) {
            stop(taskMessageOutHandler);
            stop(process);
        }
        return taskModel;
    }

    @Override
    public boolean stop(Process process) {
        if (process != null) {
            process.destroy();
            return true;
        }
        return false;
    }

    @Override
    public boolean stop(Thread outHandler) {
        if (outHandler != null && outHandler.isAlive()) {
            outHandler.interrupt();
            return true;
        }
        return false;
    }

    @Override
    public boolean stop(Process process, Thread thread) {
        boolean ret = stop(thread);
        if(ret == true){
            ret = stop(process);
        }
        return ret;
    }
}
