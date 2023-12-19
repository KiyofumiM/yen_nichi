package com.tie.yennichi.controller;

import java.security.Principal;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.entity.CommentEvent;
import com.tie.yennichi.form.CommentEventForm;
import com.tie.yennichi.repository.CommentEventRepository;

@Controller
public class CommentsEventController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentEventRepository repository;

    @RequestMapping(value = "/event/{eventId}/comments_event/new")
    public String newComment(@PathVariable("eventId") long eventId, Model model) {
        CommentEventForm form = new CommentEventForm();
        form.setEventId(eventId);
        model.addAttribute("form", form);
        return "comments_event/new";
    }

    @RequestMapping(value = "/event/{eventId}/comment_event")
    public String create(@PathVariable("eventId") long eventId, @Validated @ModelAttribute("form") CommentEventForm form,
            BindingResult result, Principal principal, Model model, RedirectAttributes redirAttrs, Locale locale) {
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "投稿に失敗しました。");
            return "comments_event/new";
        }
        
        CommentEvent entity = modelMapper.map(form, CommentEvent.class);
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        entity.setEventId(eventId);
        entity.setUserId(user.getUserId());
        repository.saveAndFlush(entity);

        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", "投稿に成功しました。");

        return "redirect:/events";
    }
}