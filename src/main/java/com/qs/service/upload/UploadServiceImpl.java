package com.qs.service.upload;

import com.qs.enums.VideoCodeEnum;
import com.qs.model.upload.UploadRecord;
import com.qs.service.ModelService;
import com.qs.service.UploadService;
import com.qs.utils.CommonUtils;
import com.qs.utils.ConvertUtil;
import com.qs.utils.QsFileUtils;
import com.qs.utils.VideoExceptionUtils;
import com.qs.vo.req.VideoUploadReqVo;
import com.qs.vo.resp.CommonRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * 文件上传service
 *
 * @author FBin
 * @time 2018/12/3 17:14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UploadServiceImpl implements UploadService {

    @Value("${server.upload.filepath}")
    private String savePath;

    @Value("${server.visit.path}")
    private String visitPath;

    @Autowired
    private ModelService modelService;

    @Override
    public CommonRespVO execUpload(MultipartFile multipartFile) {
        VideoExceptionUtils.assertNotNull(multipartFile, "上传文件未获取到！");

        File dirs = new File(savePath);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        // 给上传上来的文件一个新的名字
        String oldFileName = multipartFile.getOriginalFilename();
        String newFileName = ConvertUtil.getBase64Time() + oldFileName.substring(oldFileName.lastIndexOf("."));
        String savePathAndName = savePath + newFileName;
        File uploadFile = new File(savePathAndName);
        try {
            uploadFile.createNewFile();
            DigestUtils.md5Hex(multipartFile.getInputStream());
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), uploadFile);
            return getSuccessVO(VideoCodeEnum.UPLOAD_SUCCESS, visitPath + newFileName);
        } catch (Exception e) {
            log.error("创建目标文件异常", e);
            // 判断文件是否存在，存在即删除
            if (uploadFile.exists()) {
                uploadFile.delete();
            }
            return getErrorVO(VideoCodeEnum.UPLOAD_ERROR, e.getMessage());
        }
    }

    @Override
    public CommonRespVO uploadOneBlockFile(MultipartFile multipartFile, String targetFilePath) {

        VideoExceptionUtils.assertNotNull(multipartFile, "未获取到上传文件");

        File destTempFile = new File(targetFilePath);
        File fileParent = destTempFile.getParentFile();

        // 判断父文件夹是否存在，不存在则创建
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }

        try {
            destTempFile.createNewFile();
            DigestUtils.md5Hex(multipartFile.getInputStream());
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), destTempFile);
            return getSuccessVO(VideoCodeEnum.UPLOAD_SUCCESS, visitPath + targetFilePath);
        } catch (IOException e) {
            log.error("文件写入异常，异常原因：" + e.getMessage(), e);
            return getErrorVO(VideoCodeEnum.UPLOAD_ERROR, e.getMessage());
        }
    }

    @Override
    public CommonRespVO uploadMultiBlockFile(MultipartFile multipartFile, String blockIndex, String blockNumber, String randomUUID, String targetFilePath) {
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
                log.error("新建文件异常，异常原因：" + e.getMessage(), e);
                VideoExceptionUtils.fail("新建文件发生异常！");
            }
            QsFileUtils.fileConsistent(randomUUID, fileIndexPath, targetFilePath);
            byte[] buf = new byte[1024 * 1024 * 2];
            int len;
            RandomAccessFile fileWrite = new RandomAccessFile(destTempFile, "rw");
            InputStream fileReader = multipartFile.getInputStream();
            fileWrite.seek((Integer.valueOf(blockIndex) - 1) * 104857600);
            while ((len = fileReader.read(buf)) != -1) {
                fileWrite.write(buf, 0, len);
            }
            fileReader.close();
            fileWrite.close();

            if (QsFileUtils.fileComplete(randomUUID, fileIndexPath, Integer.valueOf(blockIndex), Integer.valueOf(blockNumber))) {
                File file = new File(targetFilePath);
                file.renameTo(new File(fileRealName));
            }
            return getSuccessVO(VideoCodeEnum.UPLOAD_SUCCESS, visitPath + targetFilePath);
        } catch (IOException e) {
            log.error("part文件写入异常，异常原因：" + e.getMessage(), e);
            return getErrorVO(VideoCodeEnum.UPLOAD_ERROR, e.getMessage());
        }
    }

    @Override
    public void saveUploadRecord(VideoUploadReqVo videoUploadReqVo) {
        UploadRecord uploadRecord = modelService.createObject(UploadRecord.class);
        uploadRecord.setCode(CommonUtils.getTimeCodeSuffix(uploadRecord.getClass()));
        uploadRecord.setExtName(videoUploadReqVo.getExtName());
        uploadRecord.setSaveName(videoUploadReqVo.getSaveName());
        uploadRecord.setOriginName(videoUploadReqVo.getOriginName());
        modelService.save(uploadRecord);
    }

    /**
     * 上传成功的VO
     *
     * @param videoCodeEnum
     * @param data
     * @return
     */
    private CommonRespVO getSuccessVO(VideoCodeEnum videoCodeEnum, Object data) {
        return CommonRespVO.builder()
                .code(videoCodeEnum.getCode())
                .msg(videoCodeEnum.getLabel())
                .data(data).build();
    }

    /**
     * 上传失败的VO
     *
     * @param videoCodeEnum
     * @param data
     * @return
     */
    private CommonRespVO getErrorVO(VideoCodeEnum videoCodeEnum, Object data){
        return CommonRespVO.builder()
                .code(videoCodeEnum.getCode())
                .msg("上传文件保存异常!")
                .data(data).build();
    }
}
