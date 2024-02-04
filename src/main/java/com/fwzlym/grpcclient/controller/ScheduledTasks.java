package com.fwzlym.grpcclient.controller;

/**
 * @AUTHOR: qiukui
 * @CREATE: 2023-10-24-22:02
 */
import com.fwzlym.grpcclient.config.OsUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

/**
 * @author shuang.kou
 */
@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行一次
    public void cleanupImages() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime oneHourAgo = currentTime.minusHours(1);
        String fileSeparator = System.getProperty("file.separator");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDate.now().format(dateFormatter);
        String[] dateStr = date.split("-");
        String imageDirectory = OsUtil.WINDOWS_PATH + fileSeparator + dateStr[0] + fileSeparator + dateStr[1];; // 修改为实际的图片存储目录

        Path directoryPath = Paths.get(imageDirectory);

        if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
            try {
                Files.walkFileTree(directoryPath, EnumSet.noneOf(FileVisitOption.class), 1, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        LocalDateTime fileCreationTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(attrs.creationTime().toMillis()), ZoneId.systemDefault());
                        if (fileCreationTime.isBefore(oneHourAgo)) {
                            if (Files.deleteIfExists(file)) {
                                System.out.println("Deleted file: " + file.toAbsolutePath());
                            } else {
                                System.out.println("Failed to delete file: " + file.toAbsolutePath());
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
                log.info("清除过期图片完成");
            } catch (IOException e) {
                e.printStackTrace();
                log.info("清除过期图片出错");
            }
        }
    }


//    @Scheduled(fixedRate = 5000)
////    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行一次
//    public void cleanupImages() {
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String date = LocalDate.now().format(dateFormatter);
//        String[] dateStr = date.split("-");
//        LocalDateTime currentTime = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String currentDateString = currentTime.format(formatter);
//        String fileSeparator = System.getProperty("file.separator");
//        String imageDirectory = OsUtil.WINDOWS_PATH + fileSeparator + dateStr[0] + fileSeparator + dateStr[1]; // 修改为实际的图片存储目录
//
//        File directory = new File(imageDirectory);
//
//        if (directory.exists() && directory.isDirectory()) {
//            File[] files = directory.listFiles();
//
//            if (files != null) {
//                for (File file : files) {
//                    String fileName = file.getName();
//                    if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
//                        String fileDateString = fileName.substring(0, 8); // 提取文件名中的日期部分
//                        if (fileDateString.compareTo(currentDateString) < 0) {
//                            if (file.delete()) {
//                                System.out.println("Deleted file: " + file.getAbsolutePath());
//                            } else {
//                                System.out.println("Failed to delete file: " + file.getAbsolutePath());
//                            }
//                        }
//                    }
//                }
//                log.info("清除过期图片完成");
//            }
//        }
//    }












//    /**
//     * fixedRate：固定速率执行。每5秒执行一次。
//     */
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTimeWithFixedRate() {
//        log.info("Current Thread : {}", Thread.currentThread().getName());
//        log.info("固定速率执行。每5秒执行一次: The time is now {}", dateFormat.format(new Date()));
//    }
//
//    /**
//     * fixedDelay：固定延迟执行。距离上一次调用成功后2秒才执。
//     */
//    @Scheduled(fixedDelay = 2000)
//    public void reportCurrentTimeWithFixedDelay() {
//        try {
//            TimeUnit.SECONDS.sleep(3);
//            log.info("固定延迟执行。距离上一次调用成功后2秒才执: The time is now {}", dateFormat.format(new Date()));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * initialDelay:初始延迟。任务的第一次执行将延迟5秒，然后将以5秒的固定间隔执行。
//     */
//    @Scheduled(initialDelay = 5000, fixedRate = 5000)
//    public void reportCurrentTimeWithInitialDelay() {
//        log.info("初始延迟。任务的第一次执行将延迟5秒，然后将以5秒的固定间隔执行: The time is now {}", dateFormat.format(new Date()));
//    }
//
//    /**
//     * cron：使用Cron表达式。　每分钟的1，2秒运行
//     */
//    @Scheduled(cron = "1-2 * * * * ? ")
//    public void reportCurrentTimeWithCronExpression() {
//        log.info("每分钟的1，2秒运行: The time is now {}", dateFormat.format(new Date()));
//    }
}
