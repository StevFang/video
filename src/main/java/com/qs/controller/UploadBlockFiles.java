package com.qs.controller;

import com.qs.service.UploadService;
import com.qs.utils.ConvertUtil;
import com.qs.utils.VideoExceptionUtils;
import com.qs.vo.resp.CommonRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 *
 * @author FBin
 * @time 2018/12/3 19:57
 */
@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("/upload")
public class UploadBlockFiles {

    @Autowired
    private UploadService uploadService;

    /**
     * 大文件分片上传服务接收端
     *
     * @param blockIndex     这次请求的是第几块
     * @param blockNumber    将文件分成的总块数
     * @param targetFilePath 要根据这个路径制定块文件的路径和生成文件的路径
     * @param randomUUID     标识是否同一个文件
     * @param multipartFile  传到后台的具体文件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/big", method = {RequestMethod.POST})
    public CommonRespVO bigFileUpload(@RequestParam(value = "blockIndex", required = false) String blockIndex,
                                      @RequestParam(value = "blockNumber", required = false) String blockNumber,
                                      @RequestParam(value = "targetFilePath", required = false) String targetFilePath,
                                      @RequestParam(value = "randomUUID", required = false) String randomUUID,
                                      @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        VideoExceptionUtils.assertNotBlank(blockNumber, "参数blockNumber不能为空！");
        CommonRespVO commonRespVO = null;
        // 文件不走分块上传
        if (ConvertUtil.getInt(blockNumber) == 1) {
            commonRespVO = uploadService.uploadOneBlockFile(multipartFile, targetFilePath);
        } else if (ConvertUtil.getInt(blockNumber) > 1) {
            commonRespVO = uploadService.uploadMultiBlockFile(multipartFile, blockIndex, blockNumber, randomUUID, targetFilePath);
        }
        return commonRespVO;
    }

}
