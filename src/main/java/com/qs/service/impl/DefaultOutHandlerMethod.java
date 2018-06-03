package com.qs.service.impl;

import com.qs.service.OutHandlerMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 默认任务消息输出处理
 * @author fbin
 */
@Component("defaultOutHandlerMethod")
public class DefaultOutHandlerMethod implements OutHandlerMethod {

	/**
	 * 记录日志
	 */
	private static Logger logger = LoggerFactory.getLogger(DefaultOutHandlerMethod.class);

	@Override
	public void parse(String appName, String msg) {
		//过滤消息
		if (msg.indexOf("[rtsp") != -1) {
			logger.error("appName=" + appName + " 发生网络异常丢包，消息体：" + msg);
		}else if(msg.indexOf("frame=")!=-1){
			logger.error("appName=" + appName + " :" + msg);
		}else{
			logger.info("appName=" + appName + " :" + msg);
		}
	}
}
