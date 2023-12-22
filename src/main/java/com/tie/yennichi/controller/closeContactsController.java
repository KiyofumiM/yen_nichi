package com.tie.yennichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class closeContactsController {

    @RequestMapping("contact/success")
    public String close() {
        return "contact/success";
    }
}