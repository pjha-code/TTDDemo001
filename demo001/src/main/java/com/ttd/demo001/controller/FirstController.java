package com.ttd.demo001.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {
    
    @GetMapping("/get")
    public String get(){
        return "get call response";
    }

}
