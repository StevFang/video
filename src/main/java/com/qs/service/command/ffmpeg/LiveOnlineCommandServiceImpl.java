package com.qs.service.command.ffmpeg;

import com.qs.common.CommonConstant;
import com.qs.dto.config.ffmpeg.DecodeDTO;
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
 * 创建ffmpeg 直播推流 指令
 *
 * @author FBin
 */
@Slf4j
@Service
public class LiveOnlineCommandServiceImpl implements CommandService<LiveOnlineDTO> {

    @Autowired
    private CommandCommonService commandCommonService;

    @Override
    public String createCommand(LiveOnlineDTO liveOnlineDTO) {
        try{
            String fastForwardMPEGPath = liveOnlineDTO.getFfmpegPath();
            if(StringUtils.isNotBlank(fastForwardMPEGPath)) {
                // -i：输入流地址或者文件绝对地址
                StringBuilder command = new StringBuilder(fastForwardMPEGPath).append(" -i ");
                // 是否有必输项：输入地址，输出地址，应用名，twoPart：0-推一个元码流；1-推一个自定义推流；2-推两个流（一个是自定义，一个是元码）
                String input = liveOnlineDTO.getInput();
                String output = liveOnlineDTO.getOutput();
                String appName = liveOnlineDTO.getAppName();
                String twoPart = liveOnlineDTO.getTwoPart();
                String codec = liveOnlineDTO.getCodec();

                // 默认h264解码
                codec = codec == null ? "h264" : codec;

                // 输入地址
                command.append(input);

                // 当twoPart为0时，只推一个元码流
                if (CommonConstant.TWO_PART_0.equals(twoPart)) {
                    command.append(" -vcodec ").append(codec).append(" -f flv -an ").append(output).append(appName);
                } else {
                    // -f ：转换格式，默认flv
                    String fmt = liveOnlineDTO.getFmt();
                    if(StringUtils.isNotBlank(fmt)){
                        command.append(" -f ").append(fmt);
                    }
                    // -r :帧率，默认25；-g :帧间隔
                    String fps = liveOnlineDTO.getFps();
                    if (StringUtils.isNotBlank(fps)) {
                        command.append(" -r ").append(fps);
                        command.append(" -g ").append(fps);
                    }
                    // -s 分辨率 默认是原分辨率
                    String rs = liveOnlineDTO.getRs();
                    if (StringUtils.isNotBlank(rs)) {
                        command.append(" -s ").append(rs);
                    }
                    // 输出地址+发布的应用名
                    command.append(" -an ")
                            .append(output)
                            .append(appName);

                    // 当twoPart为2时推两个流，一个自定义流，一个元码流
                    if (CommonConstant.TWO_PART_2.equals(twoPart)) {
                        // 一个视频源，可以有多个输出，第二个输出为拷贝源视频输出，不改变视频的各项参数并且命名为应用名+HD
                        command.append(" -vcodec copy -f flv -an ")
                                .append(output)
                                .append(appName)
                                .append("HD");
                    }
                }

                return command.toString();
            }
        }catch (Exception e){
            log.error("组装fastForwardMPEG指令发生异常", e);
            return "";
        }
        return "";
    }

    /**
     * 视频转码
     *
     * @param liveOnlineDto
     * @param liveOnlineCommand
     */
    public void liveOnlineVideo(LiveOnlineDTO liveOnlineDto, String liveOnlineCommand){
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
