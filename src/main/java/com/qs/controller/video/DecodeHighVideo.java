package com.qs.controller.video;

import com.qs.service.VideoService;
import com.qs.vo.req.DecodeHighReqVO;
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
public class DecodeHighVideo {

    @Autowired
    private VideoService videoService;

    /**
     * 视频高清转码
     * @param decodeHighReqVO 视频转高清码请求参数
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/decode/high", method = { RequestMethod.POST })
    public CommonRespVO decode(@RequestBody DecodeHighReqVO decodeHighReqVO){
        return videoService.decodeHighVideo(decodeHighReqVO);
    }

}
