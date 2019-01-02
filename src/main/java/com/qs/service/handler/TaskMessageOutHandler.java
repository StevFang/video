package com.qs.service.handler;

import com.qs.service.OutHandlerMethod;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 任务消息输出处理器
 *
 * @author fbin
 */
@Slf4j
public class TaskMessageOutHandler extends Thread {

    /**
     * 控制状态
     */
    private boolean status = true;

    /**
     * 读取输出流
     */
    private BufferedReader bufferedReader;

    /**
     * 输出类型
     */
    private String appName;

    /**
     * 消息处理方法
     */
    private OutHandlerMethod outHandlerMethod;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public TaskMessageOutHandler(InputStream is, String appName, OutHandlerMethod outHandlerMethod) {
        bufferedReader = new BufferedReader(new InputStreamReader(is));
        this.appName = appName;
        this.outHandlerMethod = outHandlerMethod;
    }


    /**
     * 重写线程终止方法，安全的关闭线程
     */
    @Override
    public void interrupt() {
        setStatus(false);
    }

    /**
     * 执行输出线程
     */
    @Override
    public void run() {
        String msg;
        try {
            while (status && (msg = bufferedReader.readLine()) != null) {
                outHandlerMethod.parse(appName, msg);
            }
        } catch (IOException e) {
            log.error("发生内部异常错误，自动关闭[" + this.getId() + "]线程");
            interrupt();
        } finally {
            if (this.isAlive()) {
                interrupt();
            }
        }
    }

}
