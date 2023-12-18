package com.tie.yennichi.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tie.yennichi.entity.GoodEvent;
import com.tie.yennichi.entity.Event;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.form.EventForm;
import com.tie.yennichi.repository.GoodEventRepository;

@Controller
public class GoodsEventController {
	@Autowired
    private MessageSource messageSource;

    @Autowired
    private GoodEventRepository repository;

    @Autowired
    private EventsController eventsController;

    @RequestMapping(value = "/good_event", method = RequestMethod.POST)
    public String create(Principal principal, @RequestParam("event_id") long eventId, RedirectAttributes redirAttrs,
            Locale locale) {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<GoodEvent> results = repository.findByUserIdAndEventId(userId, eventId);
        if (results.size() == 0) {
            GoodEvent entity = new GoodEvent();
            entity.setUserId(userId);
            entity.setEventId(eventId);
            repository.saveAndFlush(entity);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", "いいねに登録しました");
        }

        return "redirect:/event";
    }

    @RequestMapping(value = "/good_event", method = RequestMethod.DELETE)
    @Transactional
    public String destroy(Principal principal, @RequestParam("event_id") long eventId, RedirectAttributes redirAttrs,
            Locale locale) {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<GoodEvent> results = repository.findByUserIdAndEventId(userId, eventId);;
        
        if (results.size() == 1) {
            repository.deleteByUserIdAndEventId(user.getUserId(), eventId);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", "いいねから削除しました");
        }
        return "redirect:/event";
    }

}