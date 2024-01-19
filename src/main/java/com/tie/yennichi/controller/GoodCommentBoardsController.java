package com.tie.yennichi.controller;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tie.yennichi.entity.GoodCommentBoard;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.repository.GoodCommentBoardRepository;

@Controller
public class GoodCommentBoardsController {
	@Autowired
    private MessageSource messageSource;

    @Autowired
    private GoodCommentBoardRepository repository;
    
    @RequestMapping(value = "/good_comment_board", method = RequestMethod.POST)
    public String create(Principal principal, @RequestParam("comment_board_id") long commentBoardId, RedirectAttributes redirAttrs,
            Locale locale) {
    	
        Authentication authentication = (Authentication) principal;
        
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        
        List<GoodCommentBoard> results = repository.findByUserIdAndCommentBoardId(userId, commentBoardId);
        
        if (results.size() == 0) {
            GoodCommentBoard entity = new GoodCommentBoard();
            entity.setUserId(userId);
            entity.setCommentBoardId(commentBoardId);
            repository.saveAndFlush(entity);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message",
                    messageSource.getMessage("goods.create.flash", new String[] {}, locale));
        }
        return "redirect:/board";
    }

    @RequestMapping(value = "/good_comment_board", method = RequestMethod.DELETE)
    @Transactional
    public String destroy(Principal principal, @RequestParam("comment_board_id") long commentBoardId, RedirectAttributes redirAttrs,
            Locale locale) {

        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<GoodCommentBoard> results = repository.findByUserIdAndCommentBoardId(userId, commentBoardId);
        if (results.size() == 1) {
            repository.deleteByUserIdAndCommentBoardId(user.getUserId(), commentBoardId);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message",
                    messageSource.getMessage("goods.destroy.flash", new String[] {}, locale));
        }
        return "redirect:/board";
    }
}
