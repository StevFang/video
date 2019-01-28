package com.qs.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * 文件相关工具类
 *
 * @author FBin
 */
public class QsFileUtils {


    /**
     * 文件持久化
     *
     * @param randomUUID
     * @param fileIndexPath
     * @param filepath
     * @throws IOException
     */
    public static void fileConsistent(String randomUUID, String fileIndexPath, String filepath) throws IOException {
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
                RandomAccessFile fileIndexWriter = new RandomAccessFile(fileIndex, "rw");
                fileIndexWriter.seek(0);
                fileIndexWriter.write(randomUUID.getBytes());
                fileIndexWriter.close();
            }
        }
    }

    /**
     * 判断文件是否上传完成
     *
     * @param randomUUID
     * @param fileIndexPath
     * @param index
     * @param blockNumber
     * @return
     * @throws IOException
     */
    public static boolean fileComplete(String randomUUID, String fileIndexPath, int index, int blockNumber) throws IOException {
        File fileIndex = new File(fileIndexPath);

        RandomAccessFile fileIndexWrite = new RandomAccessFile(fileIndex, "rw");

        fileIndexWrite.seek(randomUUID.length() + index * 4);
        String number = index + ",";
        fileIndexWrite.write(number.getBytes());
        fileIndexWrite.seek(randomUUID.length());
        byte[] buff = new byte[4];
        // 用于保存实际读取的字节数
        int hasRead;
        // 循环读取
        StringBuilder fileContent = new StringBuilder();
        while ((hasRead = fileIndexWrite.read(buff)) > 0) {
            fileContent.append(new String(buff, 0, hasRead));
        }
        fileIndexWrite.close();
        String[] list = fileContent.toString().split(",");
        if (list.length == blockNumber) {
            fileIndex.delete();
            return true;
        }
        return false;
    }
}
