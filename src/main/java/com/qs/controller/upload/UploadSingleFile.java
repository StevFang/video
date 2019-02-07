package com.qs.controller.upload;

import com.qs.service.UploadService;
import com.qs.vo.resp.CommonRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 单个文件上传
 *
 * @author FBin
 * @time 2018/12/3 19:57
 */
@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("/upload")
public class UploadSingleFile {

    @Autowired
    private UploadService uploadService;

    /**
     * 接收文件上传
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/single", method = {RequestMethod.POST})
    public CommonRespVO uploadVideo(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("file");
        // 处理视频上传
        return uploadService.execUpload(multipartFile);
    }

}
