package com.qs.service.impl;

import com.qs.config.FfmpegDecodeConfig;
import com.qs.service.CommandService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 创建ffmpeg 视频转码 指令
 *
 */
@Component("ffmpegDecodeCommandService")
public class FFmpegDecodeCommandServiceImpl implements CommandService<FfmpegDecodeConfig> {

    /**
     * 记录日志
     */
    private static Logger logger = LoggerFactory.getLogger(FFmpegDecodeCommandServiceImpl.class);

    @Override
    public String createCommand(FfmpegDecodeConfig fFmpegDecodeConfig) {
        String sourcePath = fFmpegDecodeConfig.getSourcePath();
        // 校验是否是文件
        if(!checkfile(sourcePath)){
            return null;
        }
        // 校验视频类型
        int type = checkVideoType(sourcePath);
        if(type == 0){
            // 可以通过ffmpeg转码的视频类型
            return getFFmpegCommand(fFmpegDecodeConfig);
        }else if(type == 1){
            // 不可以通过ffmpeg转码的视频类型，可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
            sourcePath = processAVI(fFmpegDecodeConfig);
            fFmpegDecodeConfig.setSourcePath(sourcePath);
            return getFFmpegCommand(fFmpegDecodeConfig);
        }
        return null;
    }

    /**
     * 校验是否是文件
     * @param path
     * @return
     */
    private boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        }
        return true;
    }

    /**
     * 校验视频类型
     * @return
     */
    private int checkVideoType(String path) {
        String type = path.substring(path.lastIndexOf(".") + 1, path.length()).toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        if (type.equals("avi")) {
            return 0;
        } else if (type.equals("mpg")) {
            return 0;
        } else if (type.equals("wmv")) {
            return 0;
        } else if (type.equals("3gp")) {
            return 0;
        } else if (type.equals("mov")) {
            return 0;
        } else if (type.equals("mp4")) {
            return 0;
        } else if (type.equals("asf")) {
            return 0;
        } else if (type.equals("asx")) {
            return 0;
        } else if (type.equals("flv")) {
            return 0;
        }
        // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
        // 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
        else if (type.equals("wmv9")) {
            return 1;
        } else if (type.equals("rm")) {
            return 1;
        } else if (type.equals("rmvb")) {
            return 1;
        }
        return 9;
    }

    /**
     * 用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
     * @param config
     * @return
     */
    private String processAVI(FfmpegDecodeConfig config) {
        StringBuilder command = new StringBuilder();
        command.append(config.getMemcoderPath()).append(" ");
        command.append(config.getSourcePath());
        command.append(" -oac lavc -lavcopts acodec=mp3:abitrate=64 -ovc xvid -xvidencopts bitrate=600 -of avi -o ");
        command.append(config.getTargetPath() + config.getTargetName()).append(".avi");
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(command.toString());
            Process process = builder.start();
            //process.waitFor();//等待进程执行完毕
            //防止ffmpeg进程塞满缓存造成死锁
            InputStream error = process.getErrorStream();
            InputStream is = process.getInputStream();
            byte[] b = new byte[1024];
            int readbytes = -1;
            try {
                while((readbytes = error.read(b)) != -1){
                    logger.info("FFMPEG视频转换进程错误信息："+new String(b,0,readbytes));
                }
                while((readbytes = is.read(b)) != -1){
                    logger.info("FFMPEG视频转换进程输出内容为："+new String(b,0,readbytes));
                }
            }catch (IOException e2){

            }finally {
                error.close();
                is.close();
            }
            return config.getTargetPath() + config.getTargetName() + ".avi";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
     * @param config
     * @return
     */
    private String getFFmpegCommand(FfmpegDecodeConfig config) {
        String sourcePath = config.getSourcePath();
        try{
            StringBuilder command = new StringBuilder();
            command.append(config.getFfmpegPath()).append(" -i ");
            command.append(sourcePath);
            // 音频码率
            if(StringUtils.isNotBlank(config.getBitrate())){
                command.append(" -ab ").append(config.getBitrate());
            }
            // 音频采样率
            if(StringUtils.isNotBlank(config.getFreq())){
                command.append(" -ar ").append(config.getFreq());
            }
            // 视频质量
            if(StringUtils.isNotBlank(config.getQscale())){
                command.append(" -qscale ").append(config.getQscale());
            }
            // 帧速率
            if(StringUtils.isNotBlank(config.getFps())){
                command.append(" -r ").append(config.getFps());
            }
            // 分辨率
            if(StringUtils.isNotBlank(config.getRs())){
                command.append(" -s ").append(config.getRs());
            }
            command.append(" " + config.getTargetPath() + config.getTargetName() + config.getFmt());

            return command.toString();
        }catch (Exception e){
            logger.error("FFmpeg视频解码异常", e);
            return null;
        }
    }
}
