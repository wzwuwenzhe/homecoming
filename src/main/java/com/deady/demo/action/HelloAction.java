package com.deady.demo.action;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloAction {

    @RequestMapping("/hello")
    public String hello() {
        return "hello,this is a springboot demo";
    }
}