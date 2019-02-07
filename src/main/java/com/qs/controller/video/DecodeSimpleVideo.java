package com.qs.controller.video;

import com.qs.service.VideoService;
import com.qs.vo.req.DecodeSimpleReqVO;
import com.qs.vo.resp.CommonRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

/**
 * 视频转码
 *
 * @author FBin
 * @time 2019/1/28 17:05
 */
@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("/video")
public class DecodeSimpleVideo {

    @Autowired
    private VideoService videoService;

    /**
     * 视频解码转码
     * @param decodeSimpleReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/decode/simple", method = { RequestMethod.POST })
    public CommonRespVO decode(@RequestBody DecodeSimpleReqVO decodeSimpleReqVO){
        return videoService.decodeSimpleVideo(decodeSimpleReqVO);
    }

}
