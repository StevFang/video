package com.qs.controller.video;

import com.qs.dto.config.ffmpeg.LiveOnlineDTO;
import com.qs.enums.VideoCodeEnum;
import com.qs.service.VideoService;
import com.qs.utils.VideoExceptionUtils;
import com.qs.vo.req.LiveReqVO;
import com.qs.vo.resp.CommonRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

/**
 * 视频直播
 *
 * @author FBin
 * @time 2019/1/28 17:05
 */
@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("/video")
public class LiveOnlineVideo {

    @Value("${server.ffmpeg.path}")
    private String ffmpegPath;

    @Autowired
    private VideoService videoService;

    @ResponseBody
    @RequestMapping(value = "/live", method = { RequestMethod.GET, RequestMethod.POST })
    public CommonRespVO live(@RequestBody LiveReqVO liveReqVO){
        LiveOnlineDTO liveOnlineDTO = this.checkParam(liveReqVO);
        return videoService.livePushStream(liveOnlineDTO);
    }

    /**
     * 校验参数
     *
     * @param liveReqVO
     * @return
     */
    private LiveOnlineDTO checkParam(LiveReqVO liveReqVO){
        // 参数是否符合要求
        if (StringUtils.isBlank(liveReqVO.getAppName())) {
            VideoExceptionUtils.fail(String.format(VideoCodeEnum.PARAM_CHECK_ERROR.getLabel(), "appName", liveReqVO.getAppName()));
        }
        LiveOnlineDTO liveOnlineDTO = LiveOnlineDTO.getInstanceOf(liveReqVO, ffmpegPath);
        // ffmpeg环境是否配置正确
        VideoExceptionUtils.assertNotNull(liveOnlineDTO, VideoCodeEnum.CONFIG_ERROR.getLabel());
        return liveOnlineDTO;
    }
}
