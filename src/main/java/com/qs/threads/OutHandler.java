package com.qs.threads;

import com.qs.service.OutHandlerMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 任务消息输出处理器
 *
 * @author fbin
 */
public class OutHandler extends Thread {

    private static Logger logger = LoggerFactory.getLogger(OutHandler.class);

    /**
     * 控制状态
     */
    private boolean desstatus = true;

    /**
     * 读取输出流
     */
    private BufferedReader br = null;

    /**
     * 输出类型
     */
    private String id = null;

    /**
     * 消息处理方法
     */
    private OutHandlerMethod ohm;

    public void setDesStatus(boolean desStatus) {
        this.desstatus = desStatus;
    }

    public OutHandler(InputStream is, String id, OutHandlerMethod ohm) {
        br = new BufferedReader(new InputStreamReader(is));
        this.id = id;
        this.ohm = ohm;
    }


    /**
     * 重写线程终止方法，安全的关闭线程
     */
    @Override
    public void interrupt() {
        setDesStatus(false);
    }

    /**
     * 执行输出线程
     */
    @Override
    public void run() {
        String msg = null;
        try {
            logger.info(id + "开始推流！");
            while (desstatus && (msg = br.readLine()) != null) {
                ohm.parse(id, msg);
            }
        } catch (IOException e) {
            logger.error("发生内部异常错误，自动关闭[" + this.getId() + "]线程");
            interrupt();
        } finally {
            if (this.isAlive()) {
                interrupt();
            }
        }
    }

}
