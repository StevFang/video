package com.qs.service.command.ffmpeg;

import com.qs.dto.config.ffmpeg.DecodeDTO;
import com.qs.service.CommandService;
import com.qs.service.command.CommandCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 创建ffmpeg 高清类型视频转码 指令
 *
 * @author FBin
 * @time 2019/2/8 1:25
 */
@Slf4j
@Service
public class DecodeHighCommandServiceImpl implements CommandService<DecodeDTO> {

    @Autowired
    private CommandCommonService commandCommonService;

    // ffmpeg –i test.mp4 –vcodec h264 –s 352*278 –an –f m4v test.264
    @Override
    public String createCommand(DecodeDTO decodeDTO) {
        StringBuilder command = new StringBuilder();
        try{
            command.append(decodeDTO.getFfmpegPath());
            command.append(" -i ");
            command.append(decodeDTO.getSourcePath());
            command.append(" -vcodec h264 -s ");
            command.append(decodeDTO.getRs());
            command.append(" -an -f m4v ");
            command.append(decodeDTO.getTargetPath());
            command.append(decodeDTO.getTargetName());
            command.append(decodeDTO.getFmt());
            return command.toString();
        }catch (Exception e){
            log.error(e.getMessage(), e);
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
