package com.qs.ws;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fbin on 2018/5/30.
 */
@Data
public class ResultInfo {

    private static ResultInfo resultInfo = null;

    private static Lock lock = new ReentrantLock();

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
        try{
            lock.lock();
            if(resultInfo == null){
                resultInfo = new ResultInfo();
            }
            resultInfo.setCode(code);
            resultInfo.setMsg(msg);
            resultInfo.setData(null);
            return resultInfo;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 获取实例
     * @param code
     * @param msg
     * @param data
     * @return
     */
    public static ResultInfo getInstance(String code, String msg, Object data){
        getInstance(code, msg);
        resultInfo.setData(data);
        return resultInfo;
    }

}
