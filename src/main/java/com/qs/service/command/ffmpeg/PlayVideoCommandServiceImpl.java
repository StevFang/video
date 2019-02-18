package com.qs.service.command.ffmpeg;

import com.qs.dto.config.ffmpeg.LiveOnlineDTO;
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
 * 视频点播
 *
 * @author FBin
 * @time 2019/2/18 16:17
 */
@Slf4j
@Service
public class PlayVideoCommandServiceImpl implements CommandService<LiveOnlineDTO> {

    @Autowired
    private CommandCommonService commandCommonService;

    // ffmpeg -re -i C:\Users\Administrator\Desktop\snooker.mp4 -vcodec libx264 -acodec libvo_aacenc -f flv rtmp://localhost:12345/live/room
    @Override
    public String createCommand(LiveOnlineDTO liveOnlineDTO) {
        try{
            StringBuilder command = new StringBuilder();
            String fastForwardMPEGPath = liveOnlineDTO.getFfmpegPath();
            if(StringUtils.isNotBlank(fastForwardMPEGPath)){
                command.append(fastForwardMPEGPath).append(" ");
                command.append("-re ");
                command.append("-i ");
                command.append(liveOnlineDTO.getInput()).append(" ");
                command.append("-vcodec libx264 ");
                command.append("-acodec libshine ");
                command.append("-f flv ");
                command.append("rtmp://localhost:1935/live/stream");
            }
            return command.toString();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 视频推送到流媒体服务器
     *
     * @param liveOnlineDto
     * @param liveOnlineCommand
     */
    public void playVideo(LiveOnlineDTO liveOnlineDto, String liveOnlineCommand){
        Runtime runtime = Runtime.getRuntime();
        InputStream inputStream = null;
        try {
            Process process = runtime.exec(liveOnlineCommand);
            inputStream = process.getErrorStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String msg;
            while((msg = bufferedReader.readLine()) != null){
                commandCommonService.printCommandMessage(liveOnlineDto.getAppName(), msg);
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
