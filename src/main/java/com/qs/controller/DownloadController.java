package com.qs.controller;

import com.qs.service.download.DownloadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 下载服务中心
 *
 * Created by fbin on 2018/6/1.
 *
 * @author FBin
 */
@RestController
@Scope("prototype")
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    private DownloadServiceImpl downloadServiceImpl;

    /**
     * 通用下载
     * @param request
     * @param link 下载链接
     * @param path 保存地址
     * @return
     */
    @RequestMapping("/common")
    public void download(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "path", required = false) String path){

    }

    /**
     * 多线程下载
     * @param request
     * @param link 下载链接
     * @param path 保存地址
     * @return
     */
    @RequestMapping("/multi")
    public void multiDownload(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "path", required = false) String path){

    }

    /**
     * 分布式下载(并行下载)
     * @param request
     * @param link 下载链接
     * @param path 保存地址
     * @return
     */
    @RequestMapping("/distributed")
    public void distributedDownload(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "path", required = false) String path){

    }

}
