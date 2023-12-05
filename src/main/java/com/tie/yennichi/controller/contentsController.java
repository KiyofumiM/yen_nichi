package com.tie.yennichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class contentsController {
	
    @RequestMapping("/contents")
    public String index() {
        return "pages/contents";
    }
}
