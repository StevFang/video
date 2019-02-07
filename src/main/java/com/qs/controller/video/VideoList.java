package com.qs.controller.video;

import com.google.common.collect.Lists;
import com.qs.dto.common.DataTableDTO;
import com.qs.enums.VideoCodeEnum;
import com.qs.service.VideoService;
import com.qs.utils.CommonUtils;
import com.qs.vo.req.VideoReqVO;
import com.qs.vo.resp.CommonRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 获取视频列表
 *
 * @author FBin
 * @time 2019/1/28 17:05
 */
@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("/video")
public class VideoList {

    @Autowired
    private VideoService videoService;

    @ResponseBody
    @RequestMapping(value = "/all", method = { RequestMethod.GET })
    public CommonRespVO findAll(@RequestParam(value = "param", required = false) VideoReqVO videoReqVO){
        int total = videoService.findCount(videoReqVO);
        List<CommonRespVO> dataList = Lists.newArrayList();
        if(total > 0){
            dataList = videoService.findList(videoReqVO);
        }
        DataTableDTO dataTableDTO = DataTableDTO.builder()
                .total(total)
                .current(videoReqVO.getCurrent())
                .pageSize(videoReqVO.getPageSize())
                .pages(CommonUtils.calPages(total, videoReqVO.getPageSize()))
                .dataList(dataList)
                .build();
        CommonRespVO commonRespVO = CommonUtils.getVideoRespVOByCodeEnum(VideoCodeEnum.QUERY_SUCCESS);
        commonRespVO.setData(dataTableDTO);
        return commonRespVO;
    }

}
