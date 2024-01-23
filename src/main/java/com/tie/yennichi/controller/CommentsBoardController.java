package com.tie.yennichi.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Locale;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.entity.CommentBoard;
import com.tie.yennichi.entity.CommentEvent;
import com.tie.yennichi.entity.CommentLearning;
import com.tie.yennichi.form.CommentBoardForm;
import com.tie.yennichi.form.CommentEventForm;
import com.tie.yennichi.repository.CommentBoardRepository;

@Controller
public class CommentsBoardController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentBoardRepository repository;

    @RequestMapping(value = "/board/{boardId}/comments_board/new")
    public String newComment(@PathVariable("boardId") long boardId, Model model) {
        CommentBoardForm form = new CommentBoardForm();
        form.setBoardId(boardId);
        model.addAttribute("form", form);
        return "comments_board/new";
    }

    @RequestMapping(value = "/board/{boardId}/comments_board")
    public String create(@PathVariable("boardId") long boardId, @Validated @ModelAttribute("form") CommentBoardForm form,
            BindingResult result, Principal principal, Model model, RedirectAttributes redirAttrs, Locale locale) {
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", messageSource.getMessage("comments.create.flash.1", new String[] {}, locale));
            return "comments_board/new";
        }
        
        CommentBoard entity = modelMapper.map(form, CommentBoard.class);
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        entity.setBoardId(boardId);
        entity.setUserId(user.getUserId());
        entity.setDeleted(false);
        repository.saveAndFlush(entity);

        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("comments.create.flash.2", new String[] {}, locale));

        return "redirect:/board";
    }
    
    // 登録情報を取得して表示
 	@RequestMapping(value = "/comment_board/edit", method = RequestMethod.GET)
 	public String edite(Principal principal, @RequestParam("comment_board_id") long commentBoardId, Model model) {
 		CommentBoard entity = repository.findById(commentBoardId);

 		CommentBoardForm commentBoardForm = new CommentBoardForm();
 		commentBoardForm.setId(entity.getId());

 		commentBoardForm.setDescription(entity.getDescription());
 		model.addAttribute("form", commentBoardForm);
 		return "comments_board/edit";
 	}
     
 	// 投稿内容を更新
 	@RequestMapping(value = "/comment_board/update", method = RequestMethod.POST)
 	public String update(Principal principal, @Validated @ModelAttribute("form") CommentEventForm form,
 			BindingResult result, Model model, Locale locale, HttpSession session, RedirectAttributes redirAttrs, @RequestParam("id") long commentBoardId 
 			) throws IOException {

 		if (result.hasErrors()) {
 			model.addAttribute("hasMessage", true);
 			model.addAttribute("class", "alert-danger");
 			model.addAttribute("message", messageSource.getMessage("comments.edit.flash.1", new String[] {}, locale));
 			return "comments_event/edit";
 		}

 		// 更新処理
 		CommentBoard entity = repository.findById(commentBoardId);

 		String description = form.getDescription();

 		entity.setDescription(description);

 		repository.saveAndFlush(entity);

 		redirAttrs.addFlashAttribute("hasMessage", true);
 		redirAttrs.addFlashAttribute("class", "alert-info");
 		redirAttrs.addFlashAttribute("message", messageSource.getMessage("comments.edit.flash.2", new String[] {}, locale));
 		return "redirect:/board";
 	}
 	
 // 投稿内容を削除する
 	@RequestMapping(value = "/comment_board/delete", method = RequestMethod.GET)
 	public String delete(Principal principal, Model model, Locale locale, HttpSession session, RedirectAttributes redirAttrs,
 			@RequestParam("comment_board_id") long commentBoardId) throws IOException {

 		// 更新処理
 		CommentBoard entity = repository.findById(commentBoardId);

 		entity.setDeleted(true);

 		repository.saveAndFlush(entity);

 		redirAttrs.addFlashAttribute("hasMessage", true);
 		redirAttrs.addFlashAttribute("class", "alert-info");
 		redirAttrs.addFlashAttribute("message", messageSource.getMessage("comments.delete.flash.2", new String[] {}, locale));
 		return "redirect:/board";
 	}
}
