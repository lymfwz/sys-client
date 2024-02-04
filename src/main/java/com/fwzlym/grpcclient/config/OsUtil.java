package com.fwzlym.grpcclient.config;

import org.apache.tomcat.jni.OS;
import org.springframework.beans.factory.annotation.Value;

/**
 * @AUTHOR: qiukui
 * @CREATE: 2024-02-03-20:43
 */
public class OsUtil {
    private static String OS = System.getProperty("os.name").toLowerCase();
    private static final String FILE_SEPERATOR = System.getProperty("file.separator");
    public static final String REQUEST_PATH = FILE_SEPERATOR + "img-process" + FILE_SEPERATOR + "**"; // 请求地址
    public static final String WINDOWS_PATH = "D:" + FILE_SEPERATOR + "img-process"; // 本地存放资源目录的绝对路径
    public static final String LINUX_PATH = FILE_SEPERATOR + "home" + FILE_SEPERATOR + "img-process"; // 本地存放资源目录的绝对路径

    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isLinux() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("mac"));
    }
}
