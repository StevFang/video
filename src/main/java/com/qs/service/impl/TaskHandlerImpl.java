package com.qs.service.impl;

import com.qs.model.TaskModel;
import com.qs.service.OutHandlerMethod;
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
    private OutHandlerMethod ohm;

    @Override
    public TaskModel process(String appName, String command) {
        Process process = null;
        OutHandler outHandler = null;
        TaskModel tasker = null;
        try {
            if (runtime == null) {
                runtime = Runtime.getRuntime();
            }
            process = runtime.exec(command);// 执行本地命令获取任务主进程
            outHandler = new OutHandler(process.getErrorStream(), appName, ohm);
            outHandler.start();
            tasker = new TaskModel(appName, process, outHandler);
        } catch (IOException e) {
            stop(outHandler);
            stop(process);
            // 出现异常说明开启失败，返回null
            return null;
        }
        return tasker;
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
        boolean ret;
        ret = stop(thread);
        ret = stop(process);
        return ret;
    }
}
