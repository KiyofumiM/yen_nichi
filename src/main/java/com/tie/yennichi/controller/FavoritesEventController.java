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

import com.tie.yennichi.entity.FavoriteEvent;
import com.tie.yennichi.entity.Event;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.form.EventForm;
import com.tie.yennichi.repository.FavoriteEventRepository;

@Controller
public class FavoritesEventController {
	@Autowired
    private MessageSource messageSource;

    @Autowired
    private FavoriteEventRepository repository;

    @Autowired
    private EventsController eventController;

    @GetMapping(path = "/favorite_event")
    public String index(Principal principal, Model model) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        List<FavoriteEvent> event = repository.findByUserIdOrderByUpdatedAtDesc(user.getUserId());
        List<EventForm> list = new ArrayList<>();
        for (FavoriteEvent entity : event) {
            Event EventEntity = entity.getEvent();
            EventForm form = eventController.getEvent(user, EventEntity);
            list.add(form);
        }
        model.addAttribute("list", list);

        return "favorite/events";
    }

    @RequestMapping(value = "/favorite_event", method = RequestMethod.POST)
    public String create(Principal principal, @RequestParam("learning_id") long eventId, RedirectAttributes redirAttrs,
            Locale locale) {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<FavoriteEvent> results = repository.findByUserIdAndEventId(userId, eventId);
        if (results.size() == 0) {
            FavoriteEvent entity = new FavoriteEvent();
            entity.setUserId(userId);
            entity.setEventId(eventId);
            repository.saveAndFlush(entity);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", messageSource.getMessage("favorites.create.flash", new String[] {}, locale));
        }

        return "redirect:/events";
    }

    @RequestMapping(value = "/favorite_event", method = RequestMethod.DELETE)
    @Transactional
    public String destroy(Principal principal, @RequestParam("event_id") long eventId, RedirectAttributes redirAttrs,
            Locale locale) {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<FavoriteEvent> results = repository.findByUserIdAndEventId(userId, eventId);;
        
        if (results.size() == 1) {
            repository.deleteByUserIdAndEventId(user.getUserId(), eventId);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", messageSource.getMessage("favorites.destroy.flash", new String[] {}, locale));
        }
        return "redirect:/events";
    }
}
