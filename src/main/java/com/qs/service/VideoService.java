package com.qs.service;

import com.qs.common.UploadResult;
import com.qs.config.FfmpegDecodeConfig;
import com.qs.config.FfmpegOnlineConfig;
import com.qs.manager.FfmpegManagerImpl;
import com.qs.utils.ConvertUtil;
import com.qs.vo.*;
import com.qs.ws.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 视频处理业务类
 *
 * Created by fbin on 2018/5/30.
 *
 * @author FBin
 */
@Slf4j
@Service("videoService")
public class VideoService {

    @Value("${server.upload.filepath}")
    private String savePath;

    @Value("${server.visit.path}")
    private String visitPath;

    @Value("${server.ffmpeg.path}")
    private String ffmpegPath;

    @Value("${server.memcoder.path}")
    private String memcoderPath;

    @Autowired
    private FfmpegManagerImpl ffmpegManager;

    /**
     * 处理视频上传实现
     * @param request
     * @return
     */
    public ResultInfo execUpload(HttpServletRequest request) {
        String servletPath = request.getScheme()+":" + request.getServerPort()+request.getContextPath();
        UploadResult uploadResult = new UploadResult();

        ResultInfo resultInfo = ResultInfo.getInstance("0", "上传成功");
        MultipartHttpServletRequest multipartRequest= (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("file");

        if(multipartFile == null){
            resultInfo.setCode("-1");
            resultInfo.setMsg("上传文件未获取到");
        }
        File dirs = new File(savePath);
        if(!dirs.exists()){
            dirs.mkdirs();
        }
        // 给上传上来的文件一个新的名字
        String oldFileName = multipartFile.getOriginalFilename();
        String newFileName = ConvertUtil.getBase64Time() + oldFileName.substring(oldFileName.lastIndexOf("."));
        File uploadFile = new File(savePath + newFileName);
        try{
            uploadFile.createNewFile();
            DigestUtils.md5Hex(multipartFile.getInputStream());
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), uploadFile);
        }catch (Exception e){
            log.error("创建目标文件异常", e);
            resultInfo.setCode("-1");
            resultInfo.setMsg("上传文件保存异常");
            // 判断文件是否写入成功，写入成功进行删除
            if(uploadFile.exists()){
                uploadFile.delete();
            }
        }
        resultInfo.setData(uploadResult);
        return resultInfo;
    }

    /**
     * 获取视频总数量
     * @param videoReqVO
     * @return
     */
    public int findCount(VideoReqVO videoReqVO) {
        return 0;
    }

    /**
     * 获取视频列表
     * @param videoReqVO
     * @return
     */
    public List<Map<String, Object>> findList(VideoReqVO videoReqVO) {
        return new ArrayList<>();
    }

    /**
     * 视频解码转码
     * @param decodeReqVO
     * @return
     */
    public DecodeRespVO decodeVideo(DecodeReqVO decodeReqVO) {

        FfmpegDecodeConfig ffmpegDecodeConfig = FfmpegDecodeConfig.getInstanceOf(decodeReqVO, ffmpegPath, memcoderPath, savePath);

        // ffmpeg环境是否配置正确
        if (ffmpegDecodeConfig == null) {
            log.error("配置未正确加载，无法执行");
            return new DecodeRespVO(decodeReqVO.getVideoId(), "配置未正确加载，无法执行");
        }
        // 参数是否符合要求
        if (StringUtils.isBlank(ffmpegDecodeConfig.getAppName())) {
            log.error("参数不正确，无法执行");
            return new DecodeRespVO(decodeReqVO.getVideoId(), "参数不正确，无法执行");
        }
        ffmpegManager.start(ffmpegDecodeConfig);

        return new DecodeRespVO(decodeReqVO.getVideoId(), "正在处理中，请稍后");
    }

    /**
     * 在线推流
     * @param liveReqVO
     * @return
     */
    public LiveRespVO online(LiveReqVO liveReqVO) {

        FfmpegOnlineConfig ffmpegOnlineConfig = FfmpegOnlineConfig.getInstanceOf(liveReqVO, ffmpegPath);

        // ffmpeg环境是否配置正确
        if (ffmpegOnlineConfig == null) {
            log.error("配置未正确加载，无法执行");
            return new LiveRespVO(ffmpegOnlineConfig.getOutput(), "配置未正确加载，无法执行");
        }
        // 参数是否符合要求
        if (StringUtils.isBlank(ffmpegOnlineConfig.getAppName())) {
            log.error("参数不正确，无法执行");
            return new LiveRespVO(ffmpegOnlineConfig.getOutput(), "参数不正确，无法执行");
        }

        ffmpegManager.start(ffmpegOnlineConfig);

        return new LiveRespVO(ffmpegOnlineConfig.getOutput(), "推流成功");

    }
}
