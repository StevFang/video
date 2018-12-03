package com.qs.ws;

import lombok.Data;

/**
 * 通用调用返参
 *
 * Created by fbin on 2018/5/30.
 *
 * @author FBin
 */
@Data
public class ResultInfo {

    public String code;

    public String msg;

    public Object data;

    private ResultInfo(){

    }

    /**
     * 获取实例
     * @param code
     * @param msg
     * @return
     */
    public static ResultInfo getInstance(String code, String msg){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(code);
        resultInfo.setMsg(msg);
        resultInfo.setData(null);
        return resultInfo;
    }

    /**
     * 获取实例
     * @param code
     * @param msg
     * @param data
     * @return
     */
    public static ResultInfo getInstance(String code, String msg, Object data){
        ResultInfo resultInfo = getInstance(code, msg);
        resultInfo.setData(data);
        return resultInfo;
    }

}
