package com.fwzlym.grpcclient.controller;

/**
 * @AUTHOR: qiukui
 * @CREATE: 2023-10-17-17:50
 */

import com.fwzlym.grpcclient.proto.MsgProto;
import com.fwzlym.grpcclient.proto.MsgServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;


/**
 * @author xh
 * @Date 2022/10/14
 */
@RestController
@Slf4j
public class ApiController {
//    @Autowired
//    private ServletContext servletContext;

    @GrpcClient("grpc-server")
    private MsgServiceGrpc.MsgServiceBlockingStub msgStub;

    @PostMapping("/test")
    public String testImgCal(@RequestParam("image") MultipartFile file, Model model) {
        try {
            byte[] fileBytes;
            if (!file.isEmpty()) {
                try (InputStream in = file.getInputStream();
                     ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    fileBytes = os.toByteArray();
                }
                if (fileBytes != null) {
                    // 将字节数组转换为Base64编码的字符串
                    String base64Img = Base64.getEncoder().encodeToString(fileBytes);
                    // 构建RPC请求
                    log.info("请求调用");
                    MsgProto.MsgRequest msgRequest = MsgProto.MsgRequest.newBuilder()
                            .setName(base64Img)
                            .build();
                    // 调用RPC请求
                    MsgProto.MsgResponse msgResponse = msgStub.getMsg(msgRequest);
                    String msg = msgResponse.getMsg();
                    log.info("调用成功");
                    msg = msg.substring(7, msg.length() - 1);

                    return msg;
                } else {
                    return "msg 处理失败";
                }
            } else {
                log.info("上传图片失败");
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("读取图片异常");
            return "null";
        }
    }
    /*

    byte[] fileBytes = null;
        try(InputStream in = file.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream()){
            byte[] f = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(f)) != -1) {
                os.write(f,0,bytesRead);
            }
            fileBytes = os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            // 检查文件是否为空
            if (!file.isEmpty()) {
                // 将上传的文件转换为字节数组
                fileBytes = file.getBytes();
                log.info("图片转换字节数组");
                // 进一步处理fileBytes，例如将其保存到数据库或进行其他操作

            } else {
                log.info("上传图片失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("读取图片异常");
            return "";
        }
        if (fileBytes != null) {
            //log.info(new String(fileBytes));
            log.info("开始base64编码");
            String base64Img = Base64.getEncoder().encodeToString(fileBytes);
//            String base64Img = Arrays.toString(Base64.getEncoder().encode(fileBytes));
            log.info("构建rpc请求");
            MsgProto.MsgRequest msgRequest = MsgProto.MsgRequest.newBuilder()
                    .setName(base64Img)
                    .build();
            log.info("调用请求");
            MsgProto.MsgResponse msgResponse = msgStub.getMsg(msgRequest);
            log.info("成功获取结果");
            String msg = msgResponse.getMsg();
            msg = msg.substring(7, msg.length()-1);
            model.addAttribute("image_base64", msg);
            log.info("图片回传解码成功");
            byte[] decode = Base64.getDecoder().decode(msg);
            // log.info(new String(decode).substring(0, 100));
            // TODO
            // 1. 创建一个File对象，表示要写入的图像文件
            //File imageFile = new File("output_image.jpg"); // 替换为实际图像文件路径

            // 2. 使用FileOutputStream打开一个文件输出流
//            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
//                // 3. 将byte数组写入文件
//                byte[] byteArray = decode; // 替换为您的byte数组
//                fos.write(byteArray);
//
//                System.out.println("图像文件写入成功.");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            // log.info(msg);
            return msg;
        }
        log.info("msg 处理失败");
        return "msg 处理失败";
     */
}

