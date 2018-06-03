package com.qs.service;
/**
 * 输出消息处理
 * @author fbin
 */
public interface OutHandlerMethod {
	/**
	 * 解析消息
	 * @param msg
	 * @param msg 
	 */
	void parse(String appName, String msg);
}
