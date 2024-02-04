package com.fwzlym.grpcclient.service;

import com.fwzlym.grpcclient.config.OsUtil;
import com.fwzlym.grpcclient.proto.MsgProto;
import com.fwzlym.grpcclient.proto.MsgServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

/**
 * @AUTHOR: qiukui
 * @CREATE: 2024-02-03-15:52
 */
@Service
@Slf4j
public class ApiServiceImpl implements ApiService {
    @GrpcClient("grpc-server")
    private MsgServiceGrpc.MsgServiceBlockingStub msgStub;

    @Override
    public String processImg(MultipartFile file, HttpServletRequest request) {
        try {
            // 获取file图片宽 高
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            byte[] fileBytes;
            if (!file.isEmpty()) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String date = LocalDate.now().format(dateFormatter);
                String[] dateStr = date.split("-");
                HttpSession session = request.getSession();
                session.setAttribute("id", UUID.randomUUID().toString().replace("-", ""));
                String id = session.getAttribute("id").toString();
                String folder = OsUtil.isWindows() ?
                        OsUtil.WINDOWS_PATH : OsUtil.LINUX_PATH;
                String fileSeparator = System.getProperty("file.separator");
                String filePath = folder
                        + fileSeparator + dateStr[0] + fileSeparator + dateStr[1] + fileSeparator + id + ".jpg";
                // 创建文件路径
                createImgPath(filePath);

                File tempFile = new File(filePath);
                Thumbnails.of(file.getInputStream()).size(width, height).outputQuality(1.0d).toFile(tempFile);

//                double q = 0.95d;
//                Thumbnails.of(file.getInputStream()).size(1024,1024).outputQuality(q).toFile(tempFile);
//                while (tempFile.length() > 4 * 1024 * 1024) {
//                    q -= 0.05d;
//                    Thumbnails.of(file.getInputStream()).size(1024, 1024).outputQuality(q).toFile(tempFile);
//                }


                try (InputStream in = new FileInputStream(tempFile);
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
                    String ori = msgResponse.getMsg();
                    if (ori.equals("error")) {
                        return "";
                    }
                    String[] ss = ori.split(",");
                    log.info(request.getContextPath());

                    String ss1Path = "";
                    String ss2Path = "";
                    String ss3Path = "";
                    String path = "";
                    if (OsUtil.isWindows()) {
                        path = OsUtil.WINDOWS_PATH;
                    } else if (OsUtil.isLinux()) {
                        path = OsUtil.LINUX_PATH;
                    }
                    ss1Path = path + fileSeparator + dateStr[0] + fileSeparator + dateStr[1] + fileSeparator + id + "-test.jpg";
                    ss2Path = path + fileSeparator + dateStr[0] + fileSeparator + dateStr[1] + fileSeparator + id + "-canny.jpg";
                    ss3Path = path + fileSeparator + dateStr[0] + fileSeparator + dateStr[1] + fileSeparator + id + "-fusion.jpg";

                    createImgPath(ss1Path);
                    createImgPath(ss2Path);
                    createImgPath(ss3Path);
                    decodeBase64ToImage(ss[0], ss1Path);
                    decodeBase64ToImage(ss[1], ss2Path);
                    decodeBase64ToImage(ss[2], ss3Path);


//                    String imgUrl = "/" + dateStr[0] + "/" + dateStr[1] + "/" + id + "-test.jpg";
                    String imgUrl1 = fileSeparator+"img-process"+fileSeparator + dateStr[0] + fileSeparator + dateStr[1] + fileSeparator + id + ".jpg"+fileSeparator;
                    String imgUrl2 = fileSeparator+"img-process"+fileSeparator + dateStr[0] + fileSeparator + dateStr[1] + fileSeparator + id + "-test.jpg"+fileSeparator;
//                    byte[] decode = Base64.getDecoder().decode(ss[0]);
//                    // 路径static下的img
//                    String imgPath = System.getProperty("user.dir") + "/src/main/resources/static/img/" + "res.jpg";
//                    try (FileOutputStream fos = new FileOutputStream(imgPath)) {
//                        fos.write(decode);
//                        fos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


                    log.info("调用成功");

                    return imgUrl1 + "$" + imgUrl2;
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

    @Override
    public String switchImg(String imgName, HttpServletRequest request) {
        String type = "";
        switch (imgName) {
            case "img1":
                type = "test";
                break;
            case "img2":
                type = "canny";
                break;
            case "img3":
                type = "fusion";
                break;
            default:
                break;
        }
        if(type.equals("")){
            return "";
        } else{
            String fileSeparator = System.getProperty("file.separator");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date = LocalDate.now().format(dateFormatter);
            String[] dateStr = date.split("-");
            String url = fileSeparator+"img-process"+fileSeparator + dateStr[0] + fileSeparator + dateStr[1] + fileSeparator + request.getSession().getAttribute("id") + "-" + type +".jpg"+fileSeparator;
//            log.info("url: {}", url);
            return url;
        }
    }

    public static void decodeBase64ToImage(String base64String, String path) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64String);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            BufferedImage image = ImageIO.read(bis);
            if (image != null) {
                ImageIO.write(image, "jpg", new File(path));
            }
        }
    }

    public static void createImgPath(String filePath) {
        // 创建文件路径
        Path path = Paths.get(filePath);
        // 获取目录路径
        Path directoryPath = path.getParent();
        try {
            // 创建目录和文件
            Files.createDirectories(directoryPath); // 创建目录和所有父目录
            Files.createFile(path); // 创建文件
            System.out.println("路径和文件已成功创建。");
        } catch (IOException e) {
            System.err.println("无法创建路径或文件: " + e.getMessage());
        }
    }
}
