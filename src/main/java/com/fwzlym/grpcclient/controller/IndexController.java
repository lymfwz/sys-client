package com.fwzlym.grpcclient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @AUTHOR: qiukui
 * @CREATE: 2023-10-17-20:10
 */
@Controller("/")
public class IndexController {

    @GetMapping
    public String test(Model model) {
        //model.addAttribute("message", "fwzlym");
        return "index";
    }

}
