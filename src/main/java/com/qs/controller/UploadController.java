package com.qs.controller;

import com.qs.enums.VideoCodeEnum;
import com.qs.service.upload.UploadServiceImpl;
import com.qs.utils.CommonUtils;
import com.qs.utils.ConvertUtil;
import com.qs.utils.VideoExceptionUtils;
import com.qs.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

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
public class UploadController {

    @Autowired
    private UploadServiceImpl uploadService;

    /**
     * 接收文件上传
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/simple", method = {RequestMethod.POST})
    public ResultVO uploadVideo(HttpServletRequest request) {
        ResultVO resultVO = CommonUtils.getResultVOByCodeEnum(VideoCodeEnum.UPLOAD_SUCCESS);
        try {
            MultipartHttpServletRequest multipartRequest= (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartRequest.getFile("file");
            // 处理视频上传
            String url = uploadService.execUpload(multipartFile);
            resultVO.setData(url);
        } catch (Exception e) {
            log.error("上传视频接收报错,错误原因：" + e.getMessage(), e);
            VideoExceptionUtils.fail("上传失败！");
        }
        return resultVO;
    }

    /**
     * 大文件分片上传服务接收端
     *
     * @param blockIndex 这次请求的是第几块
     * @param blockNumber 将文件分成的总块数
     * @param targetFilePath 要根据这个路径制定块文件的路径和生成文件的路径
     * @param randomUUID 标识是否同一个文件
     * @param multipartFile 传到后台的具体文件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/big", method = {RequestMethod.POST})
    public ResultVO bigFileUpload(@RequestParam(value = "blockIndex", required = false) String blockIndex,
                                  @RequestParam(value = "blockNumber", required = false) String blockNumber,
                                  @RequestParam(value = "targetFilePath", required = false) String targetFilePath,
                                  @RequestParam(value = "randomUUID", required = false) String randomUUID,
                                  @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        VideoExceptionUtils.assertNotBlank(blockNumber, "参数blockNumber不能为空！");

        // 文件不走分块上传
        if(ConvertUtil.getInt(blockNumber) == 1){
            uploadService.uploadOneBlockFile(multipartFile, targetFilePath);
        }else if(ConvertUtil.getInt(blockNumber) > 1) {
            uploadService.uploadMultiBlockFile(multipartFile, blockIndex, blockNumber, randomUUID, targetFilePath);
        }

        return CommonUtils.getResultVOByCodeEnum(VideoCodeEnum.UPLOAD_SUCCESS);
    }

}
