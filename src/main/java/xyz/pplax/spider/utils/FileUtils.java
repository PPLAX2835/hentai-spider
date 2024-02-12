package xyz.pplax.spider.utils;

import java.io.File;

public class FileUtils {

    /**
     * 获得文件扩展名
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }


    /**
     * 创建目录
     * @param path 目录路径
     * @return true 如果目录创建成功，否则返回 false
     */
    public static boolean createDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return false; // 目录已经存在
    }

    /**
     * 判断文件是否存在
     * @param filePath 文件路径
     * @return true 如果文件存在，否则返回 false
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

}
