package com.qs.service;

import com.qs.common.UploadResult;
import com.qs.ws.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
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

    /**
     * 上传单个分块的文件
     *
     * @param resultInfo
     * @param multipartFile
     * @param targetFilePath
     */
    public void uploadOneBlockFile(ResultInfo resultInfo, MultipartFile multipartFile, String targetFilePath){
        UploadResult uploadResult = UploadResult.builder().build();
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
            resultInfo.setData(uploadResult);

        } catch (IOException e) {
            resultInfo.setMsg("文件写入发生错误");
            log.error("文件写入异常，异常原因：" + e.getMessage(), e);
        }
    }

    public void uploadMultiBlockFile(ResultInfo resultInfo, MultipartFile multipartFile,
                                     String blockIndex, String blockNumber, String randomUUID, String targetFilePath) {
        try {
            UploadResult uploadResult = UploadResult.builder().build();
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
                log.error("新建目录异常，异常原因：" + e.getMessage(), e);
                e.printStackTrace();
            }
            this.fileConsistent(randomUUID, fileIndexPath, targetFilePath);
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

            if (this.fileComplete(randomUUID, fileIndexPath,
                    Integer.valueOf(blockIndex),
                    Integer.valueOf(blockNumber))) {
                File file = new File(targetFilePath);
                file.renameTo(new File(fileRealName));
                resultInfo.setData(uploadResult);
            }
        } catch (IOException e) {
            log.error("part文件写入异常，异常原因：" + e.getMessage(), e);
        }
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

}
