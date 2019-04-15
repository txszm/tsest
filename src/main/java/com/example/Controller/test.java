package com.example.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@Slf4j
public class test {
    @GetMapping("test1")
    public String  test(){
        log.info("hello debug");

        return "adsdsadsa";
    }
}
