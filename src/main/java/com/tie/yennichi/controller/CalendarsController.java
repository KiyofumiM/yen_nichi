package com.tie.yennichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
* イベント情報のシェアで利用するカレンダにリンクするためのcontrollerクラス
*/
@Controller
public class CalendarsController {

    @GetMapping(path = "/calendar/events")
    public String indexEvent(Model model) {
        return "calendars/events";
    }

}
