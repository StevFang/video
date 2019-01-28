package com.qs.controller;

import com.qs.enums.VideoCodeEnum;
import com.qs.service.VideoService;
import com.qs.utils.CommonUtils;
import com.qs.vo.req.DecodeReqVO;
import com.qs.vo.resp.CommonRespVO;
import com.qs.vo.resp.DecodeRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
public class VideoDecode {

    @Autowired
    private VideoService videoService;

    /**
     * 视频解码转码
     * @param decodeReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/decode", method = { RequestMethod.POST })
    public CommonRespVO videoDecode(DecodeReqVO decodeReqVO){
        return videoService.decodeVideo(decodeReqVO);
    }

}
