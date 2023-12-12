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
import com.tie.yennichi.entity.CommentLearning;
import com.tie.yennichi.form.CommentLearningForm;
import com.tie.yennichi.repository.CommentLearningRepository;

@Controller
public class CommentsLearningController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentLearningRepository repository;

    @RequestMapping(value = "/learning/{learningId}/comments_learning/new")
    public String newComment(@PathVariable("learningId") long learningId, Model model) {
        CommentLearningForm form = new CommentLearningForm();
        form.setLearningId(learningId);
        model.addAttribute("form", form);
        return "comments_learning/new";
    }

    @RequestMapping(value = "/learning/{learningId}/comment_learning")
    public String create(@PathVariable("learningId") long learningId, @Validated @ModelAttribute("form") CommentLearningForm form,
            BindingResult result, Principal principal, Model model, RedirectAttributes redirAttrs, Locale locale) {
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "投稿に失敗しました。");
            return "comments_learning/new";
        }
        
        CommentLearning entity = modelMapper.map(form, CommentLearning.class);
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        entity.setLearningId(learningId);
        entity.setUserId(user.getUserId());
        repository.saveAndFlush(entity);

        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", "投稿に成功しました。");

        return "redirect:/learning";
    }
}