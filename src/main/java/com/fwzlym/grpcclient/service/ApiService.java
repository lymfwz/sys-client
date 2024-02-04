package com.fwzlym.grpcclient.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @AUTHOR: qiukui
 * @CREATE: 2024-02-03-15:51
 */
public interface ApiService {
    String processImg(MultipartFile file, HttpServletRequest request);

    String switchImg(String imgName, HttpServletRequest request);
}
