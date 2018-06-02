package com.qs.controller;

import com.qs.service.DownloadService;
import com.qs.ws.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 下载服务中心
 *
 * Created by fbin on 2018/6/1.
 */
@RestController
@Scope("prototype")
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    /**
     * 通用下载
     * @param request
     * @param link 下载链接
     * @param path 保存地址
     * @return
     */
    @ResponseBody
    @RequestMapping("/common")
    public ResultInfo download(
            HttpServletRequest request,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "path", required = false) String path){
        ResultInfo resultInfo = ResultInfo.getInstance("-1", "");

        return resultInfo;
    }

    /**
     * 多线程下载
     * @param request
     * @param link 下载链接
     * @param path 保存地址
     * @return
     */
    @ResponseBody
    @RequestMapping("/multi")
    public ResultInfo multiDownload(
            HttpServletRequest request,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "path", required = false) String path){
        ResultInfo resultInfo = ResultInfo.getInstance("-1", "");

        return resultInfo;
    }

    /**
     * 分布式下载(并行下载)
     * @param request
     * @param link 下载链接
     * @param path 保存地址
     * @return
     */
    @ResponseBody
    @RequestMapping("/substep")
    public ResultInfo substepDownload(
            HttpServletRequest request,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "path", required = false) String path){
        ResultInfo resultInfo = ResultInfo.getInstance("-1", "");

        return resultInfo;
    }

}
