package com.qs.service.upload;

import com.qs.service.UploadService;
import com.qs.utils.ConvertUtil;
import com.qs.utils.QSFileUtils;
import com.qs.utils.VideoExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
public class UploadServiceImpl implements UploadService {

    @Value("${server.upload.filepath}")
    private String savePath;

    @Value("${server.visit.path}")
    private String visitPath;

    @Override
    public String execUpload(MultipartFile multipartFile) {

        VideoExceptionUtils.assertNotNull(multipartFile, "上传文件未获取到！");

        File dirs = new File(savePath);
        if(!dirs.exists()){
            dirs.mkdirs();
        }
        // 给上传上来的文件一个新的名字
        String oldFileName = multipartFile.getOriginalFilename();
        String newFileName = ConvertUtil.getBase64Time() + oldFileName.substring(oldFileName.lastIndexOf("."));
        String savePathAndName = savePath + newFileName;
        File uploadFile = new File(savePathAndName);
        try{
            uploadFile.createNewFile();
            DigestUtils.md5Hex(multipartFile.getInputStream());
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), uploadFile);


        }catch (Exception e){
            log.error("创建目标文件异常", e);
            VideoExceptionUtils.fail("上传文件保存异常！");
            // 判断文件是否存在，存在即删除
            if(uploadFile.exists()){
                uploadFile.delete();
            }
        }
        return visitPath + newFileName;
    }

    @Override
    public String uploadOneBlockFile(MultipartFile multipartFile, String targetFilePath){

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
        } catch (IOException e) {
            log.error("文件写入异常，异常原因：" + e.getMessage(), e);
            VideoExceptionUtils.fail("文件写入发生错误");
        }

        return visitPath + targetFilePath;
    }

    @Override
    public String uploadMultiBlockFile(MultipartFile multipartFile, String blockIndex, String blockNumber, String randomUUID, String targetFilePath) {
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
            QSFileUtils.fileConsistent(randomUUID, fileIndexPath, targetFilePath);
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

            if (QSFileUtils.fileComplete(randomUUID, fileIndexPath, Integer.valueOf(blockIndex), Integer.valueOf(blockNumber))) {
                File file = new File(targetFilePath);
                file.renameTo(new File(fileRealName));
            }
        } catch (IOException e) {
            log.error("part文件写入异常，异常原因：" + e.getMessage(), e);
            VideoExceptionUtils.fail("文件写入异常！");
        }

        return visitPath + targetFilePath;
    }



}
