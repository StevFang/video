package com.qs.service;

import com.qs.common.UploadResult;
import com.qs.config.AbstractFFmpegDecodeConfig;
import com.qs.config.AbstractFFmpegOnlineConfig;
import com.qs.form.*;
import com.qs.manager.FfmpegManagerImpl;
import com.qs.utils.ConvertUtil;
import com.qs.ws.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResultInfo execUpload(HttpServletRequest request) throws Exception {
        String servletPath = request.getScheme()+":" + request.getServerPort()+request.getContextPath();
        UploadResult uploadResult = new UploadResult();
        uploadResult.getStart();

        ResultInfo resultInfo = ResultInfo.getInstance("0", "上传成功");
        MultipartHttpServletRequest multipartRequest= (MultipartHttpServletRequest) request;

//        List<MultipartFile> fileList = multipartRequest.getFiles("file");

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

            uploadResult.getEnd(servletPath + visitPath + newFileName);

        }catch (Exception e){
            log.error("创建目标文件异常", e);
            resultInfo.setCode("-1");
            resultInfo.setMsg("上传文件保存异常");
            // 判断文件是否写入成功，写入成功进行删除
            if(uploadFile.exists()){
                uploadFile.delete();
            }

            uploadResult.getEnd("");
        }
        resultInfo.setData(uploadResult);
        return resultInfo;
    }

    /**
     * 文件持久化
     * @param randomUUID
     * @param fileIndexPath
     * @param filepath
     * @throws IOException
     */
    public void fileConsistent(String randomUUID, String fileIndexPath,
                        String filepath) throws IOException {
        File fileIndex = new File(fileIndexPath);
        if (!fileIndex.exists()) {
            RandomAccessFile fileIndexWrite = new RandomAccessFile(fileIndex, "rw");
            fileIndexWrite.seek(0);
            fileIndexWrite.write(randomUUID.getBytes());
            fileIndexWrite.close();
        } else {
            RandomAccessFile fileIndexWrite = new RandomAccessFile(fileIndex, "rw");
            fileIndexWrite.seek(0);
            byte[] bytes = new byte[36];
            fileIndexWrite.read(bytes);
            String uuid = new String(bytes);
            fileIndexWrite.close();
            if (!uuid.equals(randomUUID)) {
                fileIndex.delete();
                File file = new File(filepath);
                file.delete();
                RandomAccessFile fileIndexWriter = new RandomAccessFile(
                        fileIndex, "rw");
                fileIndexWriter.seek(0);
                fileIndexWriter.write(randomUUID.getBytes());
                fileIndexWriter.close();
            }
        }
    }

    /**
     * 判断文件是否上传完成
     * @param randomUUID
     * @param fileIndexPath
     * @param index
     * @param blockNumber
     * @return
     * @throws IOException
     */
    public boolean fileComplete(String randomUUID, String fileIndexPath,
                                int index, int blockNumber) throws IOException {
        File fileIndex = new File(fileIndexPath);

        RandomAccessFile fileIndexWrite = new RandomAccessFile(fileIndex, "rw");

        fileIndexWrite.seek(randomUUID.length() + index * 4);
        String number = String.valueOf(index) + ",";
        fileIndexWrite.write(number.getBytes());
        fileIndexWrite.seek(randomUUID.length());
        byte[] buff = new byte[4];
        // 用于保存实际读取的字节数
        int hasRead = 0;
        // 循环读取
        String fileContent = "";
        while ((hasRead = fileIndexWrite.read(buff)) > 0) {
            fileContent = fileContent + new String(buff, 0, hasRead);
        }
        fileIndexWrite.close();
        String[] list = fileContent.split(",");
        System.out.println(list.length);
        if (list.length == blockNumber) {
            fileIndex.delete();
            return true;
        }
        return false;
    }

    /**
     * 获取视频总数量
     * @param videoForm
     * @return
     */
    public int findCount(VideoForm videoForm) {
        return 0;
    }

    /**
     * 获取视频列表
     * @param videoForm
     * @return
     */
    public List<Map<String, Object>> findList(VideoForm videoForm) {
        return new ArrayList<>();
    }

    /**
     * 视频解码转码
     * @param decodeForm
     * @return
     */
    public DecodeInfo decodeVideo(DecodeForm decodeForm) {

        AbstractFFmpegDecodeConfig ffmpegDecodeConfig = AbstractFFmpegDecodeConfig.getInstanceOf(decodeForm, ffmpegPath, memcoderPath, savePath);

        // ffmpeg环境是否配置正确
        if (ffmpegDecodeConfig == null) {
            log.error("配置未正确加载，无法执行");
            return new DecodeInfo(decodeForm.getVideoId(), "配置未正确加载，无法执行");
        }
        // 参数是否符合要求
        if (StringUtils.isBlank(ffmpegDecodeConfig.getAppName())) {
            log.error("参数不正确，无法执行");
            return new DecodeInfo(decodeForm.getVideoId(), "参数不正确，无法执行");
        }
        ffmpegManager.start(ffmpegDecodeConfig);

        return new DecodeInfo(decodeForm.getVideoId(), "正在处理中，请稍后");
    }

    /**
     * 在线推流
     * @param onlineForm
     * @return
     */
    public OnlineInfo online(OnlineForm onlineForm) {

        AbstractFFmpegOnlineConfig ffmpegOnlineConfig = AbstractFFmpegOnlineConfig.getInstanceOf(onlineForm, ffmpegPath);

        // ffmpeg环境是否配置正确
        if (ffmpegOnlineConfig == null) {
            log.error("配置未正确加载，无法执行");
            return new OnlineInfo(ffmpegOnlineConfig.getOutput(), "配置未正确加载，无法执行");
        }
        // 参数是否符合要求
        if (StringUtils.isBlank(ffmpegOnlineConfig.getAppName())) {
            log.error("参数不正确，无法执行");
            return new OnlineInfo(ffmpegOnlineConfig.getOutput(), "参数不正确，无法执行");
        }

        ffmpegManager.start(ffmpegOnlineConfig);

        return new OnlineInfo(ffmpegOnlineConfig.getOutput(), "推流成功");

    }
}
