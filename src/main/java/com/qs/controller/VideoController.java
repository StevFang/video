package com.qs.controller;

import com.qs.common.DataTable;
import com.qs.common.UploadResult;
import com.qs.service.UploadService;
import com.qs.service.VideoService;
import com.qs.utils.ConvertUtil;
import com.qs.vo.*;
import com.qs.ws.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 视频服务中心
 *
 * Created by fbin on 2018/5/30.
 *
 * @author FBin
 */
@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private UploadService uploadService;

    /**
     * 接收视频上传
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    public ResultInfo uploadVideo(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 处理视频上传
            return videoService.execUpload(request);
        } catch (Exception e) {
            log.error("上传视频接收报错,错误原因：" + e.getMessage(), e);
            return ResultInfo.getInstance("-1", "上传视频接收报错，请联系管理员");
        }
    }

    /**
     * 大文件分片上传服务接收端
     *
     * @param response
     * @param blockIndex     这次请求的是第几块
     * @param blockNumber    将文件分成的总块数
     * @param targetFilePath 要根据这个路径制定块文件的路径和生成文件的路径
     * @param randomUUID     标识是否同一个文件
     * @param multipartFile  传到后台的具体文件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/big/upload", method = {RequestMethod.POST})
    public ResultInfo bigFileUpload(HttpServletResponse response,
                                    @RequestParam(value = "blockIndex", required = false) String blockIndex,
                                    @RequestParam(value = "blockNumber", required = false) String blockNumber,
                                    @RequestParam(value = "targetFilePath", required = false) String targetFilePath,
                                    @RequestParam(value = "randomUUID", required = false) String randomUUID,
                                    @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        ResultInfo resultInfo = ResultInfo.getInstance("-1", "上传失败");
        // 文件不走分块上传
        if(ConvertUtil.getInt(blockNumber) == 1){
            uploadService.uploadOneBlockFile(resultInfo, multipartFile, targetFilePath);
        }else if(ConvertUtil.getInt(blockNumber) > 1) {
            uploadService.uploadMultiBlockFile(resultInfo, multipartFile, blockIndex, blockNumber, randomUUID, targetFilePath);
        }else{
            resultInfo.setMsg("缺少参数:blockNumber");
        }
        return resultInfo;
    }

    /**
     * 获取视频展示列表
     * @param request
     * @param videoReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/all", method = { RequestMethod.GET })
    public ResultInfo findAll(
            HttpServletRequest request,
            @RequestParam(value = "query", required = false) VideoReqVO videoReqVO){
        ResultInfo resultInfo = ResultInfo.getInstance("0", "查询成功");
        try{
            int total = videoService.findCount(videoReqVO);
            List<Map<String, Object>> datas = new ArrayList<>();
            if(total > 0){
                datas = videoService.findList(videoReqVO);
            }
            DataTable dataTable = DataTable.getInstance(videoReqVO, total, datas);
            resultInfo.setData(dataTable);
        }catch (Exception e){
            log.error("获取视频列表数据异常", e);
            resultInfo.setCode("-1");
            resultInfo.setMsg("获取视频列表数据异常,请联系管理员");
        }
        return resultInfo;
    }

    /**
     * 视频直播
     * @param request
     * @param liveReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/online")
    public ResultInfo online(HttpServletRequest request, LiveReqVO liveReqVO){
        ResultInfo resultInfo = ResultInfo.getInstance("0", "视频解码转码成功");
        try{
            LiveRespVO online = videoService.online(liveReqVO);
            resultInfo.setData(online);
        }catch (Exception e){
            log.error("视频推流异常", e);
            resultInfo.setCode("-1");
            resultInfo.setMsg("视频推流异常,请联系管理员");
        }
        return resultInfo;
    }

    /**
     * 视频解码转码
     * @param decodeReqVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/decode", method = { RequestMethod.POST })
    public ResultInfo videoDecode(DecodeReqVO decodeReqVO){
        ResultInfo resultInfo = ResultInfo.getInstance("0", "视频解码转码成功");
        try{
            DecodeRespVO decodeRespVO = videoService.decodeVideo(decodeReqVO);
            resultInfo.setData(decodeRespVO);
        }catch (Exception e){
            log.error("视频解码转码异常", e);
            resultInfo.setCode("-1");
            resultInfo.setMsg("视频解码转码异常,请联系管理员");
        }
        return resultInfo;
    }

}
