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

import com.tie.yennichi.entity.GoodBoard;
import com.tie.yennichi.entity.Board;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.form.BoardForm;
import com.tie.yennichi.repository.GoodBoardRepository;

@Controller
public class GoodBoardsController {
	@Autowired
    private MessageSource messageSource;

    @Autowired
    private GoodBoardRepository repository;

    @Autowired
    private BoardsController boarsController;

    @RequestMapping(value = "/good_board", method = RequestMethod.POST)
    public String create(Principal principal, @RequestParam("board_id") long boardId, RedirectAttributes redirAttrs,
            Locale locale) {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<GoodBoard> results = repository.findByUserIdAndBoardId(userId, boardId);
        if (results.size() == 0) {
            GoodBoard entity = new GoodBoard();
            entity.setUserId(userId);
            entity.setBoardId(boardId);
            repository.saveAndFlush(entity);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", "いいねに登録しました");
        }

        return "redirect:/board";
    }

    @RequestMapping(value = "/good_board", method = RequestMethod.DELETE)
    @Transactional
    public String destroy(Principal principal, @RequestParam("board_id") long boardId, RedirectAttributes redirAttrs,
            Locale locale) {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<GoodBoard> results = repository.deleteByUserIdAndBoardId(userId, boardId);
        
        if (results.size() == 1) {
            repository.deleteByUserIdAndBoardId(user.getUserId(), boardId);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", "いいねから削除しました");
        }
        return "redirect:/board";
    }

}
