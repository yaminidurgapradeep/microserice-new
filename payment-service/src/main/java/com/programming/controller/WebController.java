package com.programming.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@Slf4j
public class WebController {
    @GetMapping("/")
    public String index(@RequestParam Map<String,String> params, @RequestHeader Map<String,String> headers, Model model){
        log.info("index params: {}",params);
        log.info("index headers: {}",headers);
        return "index";
    }

    @GetMapping("/thankyou")
    public String thankyou(@RequestParam Map<String,String> params, @RequestHeader Map<String,String> headers, Model model){
        log.info("thankyou params: {}",params);
        log.info("thankyou headers: {}",headers);
        return "thankyou";
    }
}