package com.qs.service;

import com.qs.utils.ConvertUtil;
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
public class UploadService {

    @Value("${server.upload.filepath}")
    private String savePath;

    @Value("${server.visit.path}")
    private String visitPath;

    /**
     * 处理视频上传实现
     * @param multipartFile
     * @return
     */
    public String execUpload(MultipartFile multipartFile) {

        VideoExceptionUtils.assertNotNull(multipartFile, "上传文件未获取到！");

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
            VideoExceptionUtils.fail("上传文件保存异常！");
            // 判断文件是否存在，存在即删除
            if(uploadFile.exists()){
                uploadFile.delete();
            }
        }
        return visitPath + newFileName;
    }

    /**
     * 上传单个分块的文件
     *
     * @param multipartFile
     * @param targetFilePath
     */
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

    public String uploadMultiBlockFile(MultipartFile multipartFile, String blockIndex,
                                     String blockNumber, String randomUUID, String targetFilePath) {
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
            this.fileConsistent(randomUUID, fileIndexPath, targetFilePath);
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

            if (this.fileComplete(randomUUID, fileIndexPath,
                    Integer.valueOf(blockIndex),
                    Integer.valueOf(blockNumber))) {
                File file = new File(targetFilePath);
                file.renameTo(new File(fileRealName));
            }
        } catch (IOException e) {
            log.error("part文件写入异常，异常原因：" + e.getMessage(), e);
            VideoExceptionUtils.fail("文件写入异常！");
        }

        return visitPath + targetFilePath;
    }


    /**
     * 文件持久化
     * @param randomUUID
     * @param fileIndexPath
     * @param filepath
     * @throws IOException
     */
    private void fileConsistent(String randomUUID, String fileIndexPath,
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
    private boolean fileComplete(String randomUUID, String fileIndexPath,
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

}
