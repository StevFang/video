package com.qs.service.impl;

import com.qs.model.TaskModel;
import com.qs.service.TaskHandler;
import com.qs.threads.OutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 任务处理实现
 *
 * @author fbin
 */
@Component("taskHandler")
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
        OutHandler outHandler = null;
        TaskModel taskModel = null;
        try {
            if (runtime == null) {
                runtime = Runtime.getRuntime();
            }
            // 执行本地命令获取任务主进程
            process = runtime.exec(command);
            outHandler = new OutHandler(process.getErrorStream(), appName, defaultOutHandlerMethod);
            outHandler.start();
            taskModel = new TaskModel(appName, process, outHandler);
        } catch (IOException e) {
            stop(outHandler);
            stop(process);
            // 出现异常说明开启失败，返回null
            return null;
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

    @SuppressWarnings("deprecation")
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
