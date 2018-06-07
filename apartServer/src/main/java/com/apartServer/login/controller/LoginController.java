package com.apartServer.login.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @RequestMapping(path = "/login", produces = "text/plain")
    public ModelAndView login(@RequestParam Map<String, Object> paramMap) {
        return new ModelAndView("/login");
    }
}
