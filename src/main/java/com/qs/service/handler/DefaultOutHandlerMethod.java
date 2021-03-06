package com.qs.service.handler;

import com.qs.service.OutHandlerMethod;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 默认任务消息输出处理
 * @author fbin
 */
@Slf4j
@Component("defaultOutHandlerMethod")
public class DefaultOutHandlerMethod implements OutHandlerMethod {

    private static final String ERROR_PREFIX = "[rtsp";

    private static final String FRAME_PREFIX = "frame=";

	@Override
	public void parse(String appName, String msg) {
		//过滤消息
		if (msg.contains(ERROR_PREFIX)) {
			log.error("appName=" + appName + " 发生网络异常丢包，消息体：" + msg);
		}else if(msg.contains(FRAME_PREFIX)){
			log.error("appName=" + appName + " :" + msg);
		}else{
			log.info("appName=" + appName + " :" + msg);
		}
	}
}
