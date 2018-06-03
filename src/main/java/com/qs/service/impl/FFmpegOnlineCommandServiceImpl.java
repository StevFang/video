package com.qs.service.impl;

import com.qs.config.FFmpegOnlineConfig;
import com.qs.service.CommandService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 创建ffmpeg 直播推流 指令
 */
@Component("ffmpegOnlineCommandService")
public class FFmpegOnlineCommandServiceImpl implements CommandService<FFmpegOnlineConfig> {

    private static Logger logger = LoggerFactory.getLogger(FFmpegOnlineCommandServiceImpl.class);

    @Override
    public String createCommand(FFmpegOnlineConfig ffmpegOnlineConfig) {
        try{
            String ffmpegPath = ffmpegOnlineConfig.getFfmpegPath();
            if(StringUtils.isNotBlank(ffmpegPath)) {
                // -i：输入流地址或者文件绝对地址
                StringBuilder command = new StringBuilder(ffmpegPath).append(" -i ");
                // 是否有必输项：输入地址，输出地址，应用名，twoPart：0-推一个元码流；1-推一个自定义推流；2-推两个流（一个是自定义，一个是元码）
                String input = ffmpegOnlineConfig.getInput();
                String output = ffmpegOnlineConfig.getOutput();
                String appName = ffmpegOnlineConfig.getAppName();
                String twoPart = ffmpegOnlineConfig.getTwoPart();
                String codec = ffmpegOnlineConfig.getCodec();

                // 默认h264解码
                codec = codec == null ? "h264" : codec;

                // 输入地址
                command.append(input);

                // 当twoPart为0时，只推一个元码流
                if ("0".equals(twoPart)) {
                    command.append(" -vcodec ").append(codec).append(" -f flv -an ").append(output).append(appName);
                } else {
                    // -f ：转换格式，默认flv
                    String fmt = ffmpegOnlineConfig.getFmt();
                    if(StringUtils.isNotBlank(fmt)){
                        command.append(" -f ").append(fmt);
                    }
                    // -r :帧率，默认25；-g :帧间隔
                    String fps = ffmpegOnlineConfig.getFps();
                    if (StringUtils.isNotBlank(fps)) {
                        command.append(" -r ").append(fps);
                        command.append(" -g ").append(fps);
                    }
                    // -s 分辨率 默认是原分辨率
                    String rs = ffmpegOnlineConfig.getRs();
                    if (StringUtils.isNotBlank(rs)) {
                        command.append(" -s ").append(rs);
                    }
                    // 输出地址+发布的应用名
                    command.append(" -an ").append(output).append(appName);
                    // 当twoPart为2时推两个流，一个自定义流，一个元码流
                    if ("2".equals(twoPart)) {
                        // 一个视频源，可以有多个输出，第二个输出为拷贝源视频输出，不改变视频的各项参数并且命名为应用名+HD
                        command.append(" -vcodec copy -f flv -an ").append(output).append(appName).append("HD");
                    }
                }

                return command.toString();
            }
        }catch (Exception e){
            logger.error("组装ffmpeg指令发生异常", e);
            return "";
        }
        return "";
    }
}
