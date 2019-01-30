package com.qs.service.command.ffmpeg;

import com.qs.common.CommonConstant;
import com.qs.dto.config.LiveffmpegDTO;
import com.qs.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 创建ffmpeg 直播推流 指令
 *
 * @author FBin
 */
@Slf4j
@Service
public class FfmpegOnlineCommandServiceImpl implements CommandService<LiveffmpegDTO> {

    @Override
    public String createCommand(LiveffmpegDTO liveffmpegDTO) {
        try{
            String fastForwardMPEGPath = liveffmpegDTO.getFfmpegPath();
            if(StringUtils.isNotBlank(fastForwardMPEGPath)) {
                // -i：输入流地址或者文件绝对地址
                StringBuilder command = new StringBuilder(fastForwardMPEGPath).append(" -i ");
                // 是否有必输项：输入地址，输出地址，应用名，twoPart：0-推一个元码流；1-推一个自定义推流；2-推两个流（一个是自定义，一个是元码）
                String input = liveffmpegDTO.getInput();
                String output = liveffmpegDTO.getOutput();
                String appName = liveffmpegDTO.getAppName();
                String twoPart = liveffmpegDTO.getTwoPart();
                String codec = liveffmpegDTO.getCodec();

                // 默认h264解码
                codec = codec == null ? "h264" : codec;

                // 输入地址
                command.append(input);

                // 当twoPart为0时，只推一个元码流
                if (CommonConstant.TWO_PART_0.equals(twoPart)) {
                    command.append(" -vcodec ").append(codec).append(" -f flv -an ").append(output).append(appName);
                } else {
                    // -f ：转换格式，默认flv
                    String fmt = liveffmpegDTO.getFmt();
                    if(StringUtils.isNotBlank(fmt)){
                        command.append(" -f ").append(fmt);
                    }
                    // -r :帧率，默认25；-g :帧间隔
                    String fps = liveffmpegDTO.getFps();
                    if (StringUtils.isNotBlank(fps)) {
                        command.append(" -r ").append(fps);
                        command.append(" -g ").append(fps);
                    }
                    // -s 分辨率 默认是原分辨率
                    String rs = liveffmpegDTO.getRs();
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
}
