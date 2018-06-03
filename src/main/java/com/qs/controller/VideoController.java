package com.qs.controller;

import com.qs.common.DataTable;
import com.qs.common.UploadResult;
import com.qs.form.*;
import com.qs.service.VideoService;
import com.qs.utils.ConvertUtil;
import com.qs.ws.ResultInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 */
@RestController
@Scope("prototype")
@RequestMapping("/video")
public class VideoController {

    /**
     * 日志记录
     */
    private static Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private VideoService videoService;

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
            logger.error("上传视频接收报错,错误原因：" + e.getMessage(), e);
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
        // 记录开始时间
        UploadResult uploadResult = new UploadResult();
        uploadResult.getStart();
        ResultInfo resultInfo = ResultInfo.getInstance("-1", "上传失败");
        // 文件不走分块上传
        if(ConvertUtil.getInt(blockNumber) == 1){
            File destTempFile = new File(targetFilePath);
            File fileParent = destTempFile.getParentFile();
            // 判断父文件夹是否存在，不存在则创建
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            try {
                destTempFile.createNewFile();
                if (multipartFile == null) {
                    resultInfo.setMsg("未获取到上传文件");
                }
                DigestUtils.md5Hex(multipartFile.getInputStream());
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), destTempFile);
                // 设置上传成功的返回信息
                uploadResult.getEnd(targetFilePath);
                resultInfo.setData(uploadResult);

            } catch (IOException e) {
                resultInfo.setMsg("文件写入发生错误");
                logger.error("文件写入异常，异常原因：" + e.getMessage(), e);
                e.printStackTrace();
            }
        }else if(ConvertUtil.getInt(blockNumber) > 1) {
            try {
                String fileRealName = targetFilePath;
                String fileIndexPath = targetFilePath + ".index";
                targetFilePath = targetFilePath + ".part";
                File destTempFile = new File(targetFilePath);
                File fileParent = destTempFile.getParentFile();
                // 判断父文件夹是否存在，不存在则创建
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                try {
                    destTempFile.createNewFile();
                } catch (IOException e) {
                    resultInfo.setMsg("文件写入发生错误");
                    logger.error("新建目录异常，异常原因：" + e.getMessage(), e);
                    e.printStackTrace();
                }
                videoService.fileConsistent(randomUUID, fileIndexPath, targetFilePath);
                byte[] buf = new byte[1024 * 1024 * 2];
                int len = 0;
                RandomAccessFile fileWrite = new RandomAccessFile(destTempFile, "rw");
                InputStream fileReader = multipartFile.getInputStream();
                fileWrite.seek((Integer.valueOf(blockIndex) - 1) * 104857600);
                while ((len = fileReader.read(buf)) != -1) {
                    fileWrite.write(buf, 0, len);
                }
                fileReader.close();
                fileWrite.close();

                if (videoService.fileComplete(randomUUID, fileIndexPath,
                        Integer.valueOf(blockIndex),
                        Integer.valueOf(blockNumber))) {
                    File file = new File(targetFilePath);
                    file.renameTo(new File(fileRealName));
                    // 设置上传成功的返回信息
                    uploadResult.getEnd(targetFilePath);
                    resultInfo.setData(uploadResult);
                }
            } catch (IOException e) {
                logger.error("part文件写入异常，异常原因：" + e.getMessage(), e);
                e.printStackTrace();
            }
        }else{
            resultInfo.setMsg("缺少参数:blockNumber");
        }
        return resultInfo;
    }

    /**
     * 获取视频展示列表
     * @param request
     * @param videoForm
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/all", method = { RequestMethod.GET })
    public ResultInfo findAll(
            HttpServletRequest request,
            @RequestParam(value = "query", required = false) VideoForm videoForm){
        ResultInfo resultInfo = ResultInfo.getInstance("0", "查询成功");
        try{
            int total = videoService.findCount(videoForm);
            List<Map<String, Object>> datas = new ArrayList<>();
            if(total > 0){
                datas = videoService.findList(videoForm);
            }
            DataTable dataTable = DataTable.getInstance(videoForm, total, datas);
            resultInfo.setData(dataTable);
        }catch (Exception e){
            logger.error("获取视频列表数据异常", e);
            resultInfo.setCode("-1");
            resultInfo.setMsg("获取视频列表数据异常,请联系管理员");
        }
        return resultInfo;
    }

    /**
     * 视频直播
     * @param request
     * @param onlineForm
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/online")
    public ResultInfo online(HttpServletRequest request, OnlineForm onlineForm){
        ResultInfo resultInfo = ResultInfo.getInstance("0", "视频解码转码成功");
        try{
            OnlineInfo online = videoService.online(onlineForm);
            resultInfo.setData(online);
        }catch (Exception e){
            logger.error("视频推流异常", e);
            resultInfo.setCode("-1");
            resultInfo.setMsg("视频推流异常,请联系管理员");
        }
        return resultInfo;
    }

    /**
     * 视频解码转码
     * @param request
     * @param decodeForm
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/decode", method = { RequestMethod.POST })
    public ResultInfo videoDecode(
            HttpServletRequest request, DecodeForm decodeForm){
        ResultInfo resultInfo = ResultInfo.getInstance("0", "视频解码转码成功");
        try{
            DecodeInfo decodeInfo = videoService.decodeVideo(decodeForm);
            resultInfo.setData(decodeInfo);
        }catch (Exception e){
            logger.error("视频解码转码异常", e);
            resultInfo.setCode("-1");
            resultInfo.setMsg("视频解码转码异常,请联系管理员");
        }
        return resultInfo;
    }

}
