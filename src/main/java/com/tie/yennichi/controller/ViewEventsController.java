package com.tie.yennichi.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tie.yennichi.entity.Event;
import com.tie.yennichi.form.ViewEventFrom;
import com.tie.yennichi.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/view_events")
public class ViewEventsController {
	@Autowired
    private EventRepository repository;

    /**
    * イベントカレンダーに表示するEvent情報を取得する。
    *
    * @return Event情報をjsonエンコードした文字列
    */
    @GetMapping(value = "/all")
    public String getEvents() {
        String jsonMsg = null;
        try {
            Iterable<Event> events = repository.findAll();
            List<ViewEventFrom> form = new ArrayList<ViewEventFrom>();
            
            System.out.println("投稿テスト");
            for (Event entity : events) {
            	ViewEventFrom event = new ViewEventFrom();
                event.setTitle(entity.getTitle());
                event.setStart(entity.getEvent_at());
                event.setUrl("/events#" + entity.getId());
                form.add(event);
            }
            ObjectMapper mapper = new ObjectMapper();
            jsonMsg = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(form);
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
        return jsonMsg;
    }
}