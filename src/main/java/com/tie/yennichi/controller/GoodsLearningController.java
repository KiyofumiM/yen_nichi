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

import com.tie.yennichi.entity.GoodLearning;
import com.tie.yennichi.entity.Learning;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.form.LearningForm;
import com.tie.yennichi.repository.GoodLearningRepository;

@Controller
public class GoodsLearningController {
	@Autowired
    private MessageSource messageSource;

    @Autowired
    private GoodLearningRepository repository;

    @Autowired
    private LearningController learningController;

    @RequestMapping(value = "/goodL", method = RequestMethod.POST)
    public String create(Principal principal, @RequestParam("learning_id") long learningId, RedirectAttributes redirAttrs,
            Locale locale) {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<GoodLearning> results = repository.findByUserIdAndLearningId(userId, learningId);
        if (results.size() == 0) {
            GoodLearning entity = new GoodLearning();
            entity.setUserId(userId);
            entity.setLearningId(learningId);
            repository.saveAndFlush(entity);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", messageSource.getMessage("goods.create.flash", new String[] {}, locale));
        }

        return "redirect:/learning";
    }

    @RequestMapping(value = "/goodL", method = RequestMethod.DELETE)
    @Transactional
    public String destroy(Principal principal, @RequestParam("learning_id") long learningId, RedirectAttributes redirAttrs,
            Locale locale) {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<GoodLearning> results = repository.findByUserIdAndLearningId(userId, learningId);;
        
        if (results.size() == 1) {
            repository.deleteByUserIdAndLearningId(user.getUserId(), learningId);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", messageSource.getMessage("goods.destroy.flash", new String[] {}, locale));
        }
        return "redirect:/learning";
    }

}
