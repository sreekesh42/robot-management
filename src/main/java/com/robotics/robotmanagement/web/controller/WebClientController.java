package com.robotics.robotmanagement.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebClientController {

    @RequestMapping("/")
    public String welcome() {
        return "index";
    }
}
