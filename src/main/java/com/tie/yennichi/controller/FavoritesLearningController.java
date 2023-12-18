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

import com.tie.yennichi.entity.FavoriteLearning;
import com.tie.yennichi.entity.Learning;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.form.LearningForm;
import com.tie.yennichi.repository.FavoriteLearningRepository;

@Controller
public class FavoritesLearningController {
	@Autowired
    private MessageSource messageSource;

    @Autowired
    private FavoriteLearningRepository repository;

    @Autowired
    private LearningController learningController;

    @GetMapping(path = "/favorite_learning")
    public String index(Principal principal, Model model) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        List<FavoriteLearning> topics = repository.findByUserIdOrderByUpdatedAtDesc(user.getUserId());
        List<LearningForm> list = new ArrayList<>();
        for (FavoriteLearning entity : topics) {
            Learning LearningEntity = entity.getLearning();
            LearningForm form = learningController.getLearning(user, LearningEntity);
            list.add(form);
        }
        model.addAttribute("list", list);

        return "favorite/learning";
    }

    @RequestMapping(value = "/favorite", method = RequestMethod.POST)
    public String create(Principal principal, @RequestParam("learning_id") long learningId, RedirectAttributes redirAttrs,
            Locale locale) {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<FavoriteLearning> results = repository.findByUserIdAndLearningId(userId, learningId);
        if (results.size() == 0) {
            FavoriteLearning entity = new FavoriteLearning();
            entity.setUserId(userId);
            entity.setLearningId(learningId);
            repository.saveAndFlush(entity);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", "お気に入りに登録しました");
        }

        return "redirect:/learning";
    }

    @RequestMapping(value = "/favorite", method = RequestMethod.DELETE)
    @Transactional
    public String destroy(Principal principal, @RequestParam("learning_id") long learningId, RedirectAttributes redirAttrs,
            Locale locale) {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<FavoriteLearning> results = repository.findByUserIdAndLearningId(userId, learningId);;
        
        if (results.size() == 1) {
            repository.deleteByUserIdAndLearningId(user.getUserId(), learningId);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", "お気に入りから削除しました");
        }
        return "redirect:/learning";
    }
}