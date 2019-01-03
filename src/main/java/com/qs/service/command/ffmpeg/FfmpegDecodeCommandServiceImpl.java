package com.qs.service.command.ffmpeg;

import com.qs.dto.config.FastForwardMovingPictureExpertsGroupDecodeDTO;
import com.qs.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 创建ffmpeg 视频转码 指令
 *
 * @author FBin
 */
@Slf4j
@Service
public class FfmpegDecodeCommandServiceImpl implements CommandService<FastForwardMovingPictureExpertsGroupDecodeDTO> {

    @Override
    public String createCommand(FastForwardMovingPictureExpertsGroupDecodeDTO config) {
        String sourcePath = config.getSourcePath();
        // 校验是否是文件
        if(!checkFile(sourcePath)){
            return null;
        }
        // 校验视频类型
        int type = checkVideoType(sourcePath);
        if(type == 0){
            // 可以通过ffmpeg转码的视频类型
            return getFastForwardMPEGCommand(config);
        }else if(type == 1){
            // 不可以通过ffmpeg转码的视频类型，可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
            sourcePath = processAVI(config);
            config.setSourcePath(sourcePath);
            return getFastForwardMPEGCommand(config);
        }
        return null;
    }

    /**
     * 校验是否是文件
     * @param path
     * @return
     */
    private boolean checkFile(String path) {
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
    private String processAVI(FastForwardMovingPictureExpertsGroupDecodeDTO config) {
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
            try {
                int readBytes;
                while((readBytes = error.read(b)) != -1){
                    log.info("FFMPEG视频转换进程错误信息："+new String(b,0,readBytes));
                }
                while((readBytes = is.read(b)) != -1){
                    log.info("FFMPEG视频转换进程输出内容为："+new String(b,0,readBytes));
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
    private String getFastForwardMPEGCommand(FastForwardMovingPictureExpertsGroupDecodeDTO config) {
        String sourcePath = config.getSourcePath();
        try{
            StringBuilder command = new StringBuilder();
            command.append(config.getFfmpegPath())
                    .append(" -i ")
                    .append(sourcePath);
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
            command.append(" ")
                    .append(config.getTargetPath())
                    .append(config.getTargetName())
                    .append(config.getFmt());

            return command.toString();
        }catch (Exception e){
            log.error("FFmpeg视频解码异常", e);
            return null;
        }
    }
}
