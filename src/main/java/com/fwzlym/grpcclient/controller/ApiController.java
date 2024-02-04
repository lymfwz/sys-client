package com.fwzlym.grpcclient.controller;

/**
 * @AUTHOR: qiukui
 * @CREATE: 2023-10-17-17:50
 */

import com.fwzlym.grpcclient.proto.MsgProto;
import com.fwzlym.grpcclient.proto.MsgServiceGrpc;
import com.fwzlym.grpcclient.service.ApiService;
import com.google.logging.type.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @author xh
 * @Date 2022/10/14
 */
@RestController
@Slf4j
public class ApiController {
    @Autowired
    private ApiService apiService;

    @PostMapping("/test")
    public String testImgCal(@RequestParam("image") MultipartFile file, HttpServletRequest request) {
        return apiService.processImg(file, request);
    }

    @GetMapping("/switchImgUrl")
    public String switchImg(@RequestParam("imgName") String imgName, HttpServletRequest request) {
        return apiService.switchImg(imgName, request);
    }
}

