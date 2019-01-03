package com.qs.utils;

import com.qs.model.base.SysUser;

import javax.servlet.http.HttpSession;

/**
 * session上下文工具类
 *
 * @author FBin
 * @time 2019/1/3 23:01
 */
public class SessionUtils {

    public static final String VIDEO_USER = "VideoUser";

    /**
     * 判断是否在线
     *
     * @param ssoSessionId
     * @return
     */
    public boolean checkOnline(String ssoSessionId){
        HttpSession httpSession = (HttpSession) RedisUtils.get(ssoSessionId);
        if(httpSession != null){
            SysUser sysUser = (SysUser) httpSession.getAttribute(SessionUtils.VIDEO_USER);
            if(sysUser != null){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取系统当前用户
     *
     * @param ssoSessionId
     * @return
     */
    public SysUser getCurrentUser(String ssoSessionId){
        HttpSession httpSession = (HttpSession) RedisUtils.get(ssoSessionId);
        if(httpSession != null){
            return (SysUser) httpSession.getAttribute(SessionUtils.VIDEO_USER);
        }
        return null;
    }

}
