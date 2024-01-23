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
import com.tie.yennichi.entity.CommentEvent;
import com.tie.yennichi.entity.CommentLearning;
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
            model.addAttribute("message", messageSource.getMessage("comments.create.flash.1", new String[] {}, locale));
            return "comments_event/new";
        }
        
        CommentEvent entity = modelMapper.map(form, CommentEvent.class);
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        entity.setEventId(eventId);
        entity.setUserId(user.getUserId());
        entity.setDeleted(false);
        repository.saveAndFlush(entity);

        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("comments.create.flash.2", new String[] {}, locale));

        return "redirect:/events";
    }
    
	// 登録情報を取得して表示
	@RequestMapping(value = "/comment_event/edit", method = RequestMethod.GET)
	public String edite(Principal principal, @RequestParam("comment_event_id") long commentEventId, Model model) {
		CommentEvent entity = repository.findById(commentEventId);

		CommentEventForm commentEventForm = new CommentEventForm();
		commentEventForm.setId(entity.getId());

		commentEventForm.setDescription(entity.getDescription());
		model.addAttribute("form", commentEventForm);
		return "comments_event/edit";
	}
    
	// 投稿内容を更新
	@RequestMapping(value = "/comment_event/update", method = RequestMethod.POST)
	public String update(Principal principal, @Validated @ModelAttribute("form") CommentEventForm form,
			BindingResult result, Model model, Locale locale, HttpSession session, RedirectAttributes redirAttrs, @RequestParam("id") long commentEventId 
			) throws IOException {

		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", messageSource.getMessage("comments.edit.flash.1", new String[] {}, locale));
			return "comments_event/edit";
		}

		// 更新処理
		CommentEvent entity = repository.findById(commentEventId);

		String description = form.getDescription();

		entity.setDescription(description);

		repository.saveAndFlush(entity);

		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", messageSource.getMessage("comments.edit.flash.2", new String[] {}, locale));
		return "redirect:/events";
	}
	
	// 投稿内容を削除する
		@RequestMapping(value = "/comment_event/delete", method = RequestMethod.GET)
		public String delete(Principal principal, Model model, Locale locale, HttpSession session, RedirectAttributes redirAttrs,
				@RequestParam("comment_event_id") long commentEventId) throws IOException {

			// 更新処理
			CommentEvent entity = repository.findById(commentEventId);

			entity.setDeleted(true);

			repository.saveAndFlush(entity);

			redirAttrs.addFlashAttribute("hasMessage", true);
			redirAttrs.addFlashAttribute("class", "alert-info");
			redirAttrs.addFlashAttribute("message", messageSource.getMessage("comments.delete.flash.2", new String[] {}, locale));
			return "redirect:/events";
		}
}