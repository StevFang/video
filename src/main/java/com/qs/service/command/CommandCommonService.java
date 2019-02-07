package com.qs.service.command;

import com.qs.enums.base.VideoTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author FBin
 * @time 2019/2/7 22:47
 */
@Slf4j
@Service
public class CommandCommonService {

    private static final String ERROR_PREFIX = "[rtsp";

    private static final String FRAME_PREFIX = "frame=";

    /**
     * 校验是否是文件
     *
     * @param path
     * @return
     */
    public boolean checkFile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        }
        return true;
    }

    /**
     * 归类视频的类型
     *
     * @param videoPath
     * @return
     */
    public int getVideoType(String videoPath) {
        String type = videoPath.substring(videoPath.lastIndexOf(".") + 1, videoPath.length()).toLowerCase();
        try{
            VideoTypeEnum videoTypeEnum = null;
            for(VideoTypeEnum videoType : VideoTypeEnum.values()){
                if(videoType.getValue().equals(type)){
                    videoTypeEnum = videoType;
                    break;
                }
            }
            /**
             * ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
             * 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
             * 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
             */
            switch (videoTypeEnum){
                case AVI:
                case MPG:
                case WMV:
                case THREE_GP:
                case MOV:
                case MP_FOUR:
                case ASF:
                case ASX:
                case FLV:
                    return 0;
                case WMV_NINE:
                case RM:
                case RMVB:
                    return 1;
                default: return 9;
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return 9;
        }
    }

    /**
     * 打印命令消息
     *
     * @param appName
     * @param msg
     */
    public void printCommandMessage(String appName, String msg) {
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
