package com.kth.pattern.template;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempleteController {

    @RequestMapping("/templete")
    public String index() {
        return "test";
    }
}