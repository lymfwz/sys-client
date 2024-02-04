package com.fwzlym.grpcclient.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.OS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

import static com.fwzlym.grpcclient.config.OsUtil.isLinux;

/**
 * @AUTHOR: qiukui
 * @CREATE: 2024-02-03-20:28
 */
@Slf4j
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = "";
        if(OsUtil.isWindows()) {
            path = OsUtil.WINDOWS_PATH;
        } else if(isLinux()) {
            path = OsUtil.LINUX_PATH;
        }
        log.info("path: {}", path);
        if (path != null && !path.equals("") ) {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            registry.addResourceHandler("/img-process/**").addResourceLocations("file:" + path +"/");
        }

    }
}
