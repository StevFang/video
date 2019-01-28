package com.qs.service;

import com.qs.vo.req.VideoUploadReqVo;
import com.qs.vo.resp.CommonRespVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传接口
 *
 * @author FBin
 * @time 2019/1/2 15:53
 */
public interface UploadService {

    /**
     * 处理视频上传实现
     *
     * @param multipartFile
     * @return
     */
    CommonRespVO execUpload(MultipartFile multipartFile);

    /**
     * 上传单个分块的文件
     *
     * @param multipartFile
     * @param targetFilePath
     * @return
     */
    CommonRespVO uploadOneBlockFile(MultipartFile multipartFile, String targetFilePath);

    /**
     * 上传多个分块的文件
     *
     * @param multipartFile
     * @param blockIndex
     * @param blockNumber
     * @param randomUUID
     * @param targetFilePath
     * @return
     */
    CommonRespVO uploadMultiBlockFile(MultipartFile multipartFile,
                                      String blockIndex,
                                      String blockNumber,
                                      String randomUUID,
                                      String targetFilePath);

    /**
     * 保存上传记录信息
     *
     * @param videoUploadReqVo
     */
    void saveUploadRecord(VideoUploadReqVo videoUploadReqVo);
}
