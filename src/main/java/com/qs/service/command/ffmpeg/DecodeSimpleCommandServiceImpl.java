package com.qs.service.command.ffmpeg;

import com.qs.dto.config.ffmpeg.DecodeDTO;
import com.qs.service.CommandService;
import com.qs.service.command.CommandCommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 创建ffmpeg 普清类型视频转码 指令
 *
 * @author FBin
 */
@Slf4j
@Service
public class DecodeSimpleCommandServiceImpl implements CommandService<DecodeDTO> {

    @Autowired
    private CommandCommonService commandCommonService;

    @Override
    public String createCommand(DecodeDTO decodeDTO) {
        String sourcePath = decodeDTO.getSourcePath();
        // 校验是否是文件
        if(!commandCommonService.checkFile(sourcePath)){
            return null;
        }
        // 校验视频类型
        int type = commandCommonService.getVideoType(sourcePath);
        switch (type){
            case 0:
                // 可以通过ffmpeg转码的视频类型
                return getFastForwardMPEGCommand(decodeDTO);
            case 1:
                // 不可以通过ffmpeg转码的视频类型，可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
                sourcePath = processAVI(decodeDTO);
                decodeDTO.setSourcePath(sourcePath);
                return getFastForwardMPEGCommand(decodeDTO);
            default: return null;
        }
    }

    /**
     * 用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
     * @param decodeDTO
     * @return
     */
    private String processAVI(DecodeDTO decodeDTO) {
        StringBuilder command = new StringBuilder();
        command.append(decodeDTO.getMemcoderPath()).append(" ");
        command.append(decodeDTO.getSourcePath());
        command.append(" -oac lavc -lavcopts acodec=mp3:abitrate=64 -ovc xvid -xvidencopts bitrate=600 -of avi -o ");
        command.append(decodeDTO.getTargetPath() + decodeDTO.getTargetName()).append(".avi");
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
            return decodeDTO.getTargetPath() + decodeDTO.getTargetName() + ".avi";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
     * @param decodeDTO
     * @return
     */
    private String getFastForwardMPEGCommand(DecodeDTO decodeDTO) {
        String sourcePath = decodeDTO.getSourcePath();
        try{
            StringBuilder command = new StringBuilder();
            command.append(decodeDTO.getFfmpegPath())
                    .append(" -i ")
                    .append(sourcePath);
            // 音频码率
            if(StringUtils.isNotBlank(decodeDTO.getBitrate())){
                command.append(" -ab ").append(decodeDTO.getBitrate());
            }
            // 音频采样率
            if(StringUtils.isNotBlank(decodeDTO.getFreq())){
                command.append(" -ar ").append(decodeDTO.getFreq());
            }
            // 视频质量
            if(StringUtils.isNotBlank(decodeDTO.getQscale())){
                command.append(" -q:a ").append(decodeDTO.getQscale());
            }
            // 帧速率
            if(StringUtils.isNotBlank(decodeDTO.getFps())){
                command.append(" -r ").append(decodeDTO.getFps());
            }
            // 分辨率
            if(StringUtils.isNotBlank(decodeDTO.getRs())){
                command.append(" -s ").append(decodeDTO.getRs());
            }
            command.append(" ")
                    .append(decodeDTO.getTargetPath())
                    .append(decodeDTO.getTargetName())
                    .append(decodeDTO.getFmt());

            return command.toString();
        }catch (Exception e){
            log.error("FFmpeg视频解码异常", e);
            return null;
        }
    }

    /**
     * 视频转码
     *
     * @param decodeDTO
     * @param decodeCommand
     */
    public void decodeVideo(DecodeDTO decodeDTO, String decodeCommand){
        Runtime runtime = Runtime.getRuntime();
        InputStream inputStream = null;
        try {
            Process process = runtime.exec(decodeCommand);
            inputStream = process.getErrorStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String msg;
            while((msg = bufferedReader.readLine()) != null){
                commandCommonService.printCommandMessage(decodeDTO.getAppName(), msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } finally {
            try{
                if(inputStream != null){
                    inputStream.close();
                }
            }catch (Exception e){

            }
        }
    }
}
