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
import com.tie.yennichi.form.CommentEventForm;
import com.tie.yennichi.repository.CommentEventRepository;

/**
 * イベント情報のシェアで投稿するコメントの処理用controller群
 */
@Controller
public class CommentsEventController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CommentEventRepository repository;

	/**
	 * 新規投稿画面にリンクする
	 * 
	 * @param eventId, Model
	 * @return ページアドレス /comments_event/new
	 * @throws なし
	 */
	@RequestMapping(value = "/event/{eventId}/comments_event/new")
	public String newComment(@PathVariable("eventId") long eventId, Model model) {
		CommentEventForm form = new CommentEventForm();
		form.setEventId(eventId);
		model.addAttribute("form", form);
		return "comments_event/new";
	}

	/**
	 * 投稿処理
	 * 
	 * @param eventId, form : CommentEventForm, BindingResult, Principal, Model,
	 *                 RedirectAttributes, Locale
	 * @return redirect:/events
	 * @throws なし
	 */
	@RequestMapping(value = "/event/{eventId}/comment_event")
	public String create(@PathVariable("eventId") long eventId,
			@Validated @ModelAttribute("form") CommentEventForm form, BindingResult result, Principal principal,
			Model model, RedirectAttributes redirAttrs, Locale locale) {
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
		redirAttrs.addFlashAttribute("message",
				messageSource.getMessage("comments.create.flash.2", new String[] {}, locale));

		return "redirect:/events";
	}

	/**
	 * 参照した投稿内容の詳細を取得して表示
	 * 
	 * @param Principal, comment_event_id, Model
	 * @return ページアドレス /comments_event/edit
	 * @throws なし
	 */
	@RequestMapping(value = "/comment_event/edit", method = RequestMethod.GET)
	public String edite(Principal principal, @RequestParam("comment_event_id") long commentEventId, Model model) {
		CommentEvent entity = repository.findById(commentEventId);

		CommentEventForm commentEventForm = new CommentEventForm();
		commentEventForm.setId(entity.getId());

		commentEventForm.setDescription(entity.getDescription());
		model.addAttribute("form", commentEventForm);
		return "comments_event/edit";
	}

	/**
	 * 投稿内容の更新処理
	 * 
	 * @param rincipal, form : CommentEventForm, BindingResult, Model, Locale,
	 *                  RedirectAttributes, id
	 * @return redirect:/events
	 * @throws IOException
	 */
	@RequestMapping(value = "/comment_event/update", method = RequestMethod.POST)
	public String update(Principal principal, @Validated @ModelAttribute("form") CommentEventForm form,
			BindingResult result, Model model, Locale locale, HttpSession session, RedirectAttributes redirAttrs,
			@RequestParam("id") long commentEventId) throws IOException {

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
		redirAttrs.addFlashAttribute("message",
				messageSource.getMessage("comments.edit.flash.2", new String[] {}, locale));
		return "redirect:/events";
	}

	/**
	 * 投稿内容の論理削除処理
	 * 
	 * @param rincipal, form : CommentEventForm, BindingResult, Model, Locale,
	 *                  RedirectAttributes, id
	 * @return redirect:/events
	 * @throws IOException
	 */
	@RequestMapping(value = "/comment_event/delete", method = RequestMethod.GET)
	public String delete(Principal principal, Model model, Locale locale, HttpSession session,
			RedirectAttributes redirAttrs, @RequestParam("comment_event_id") long commentEventId) throws IOException {

		// 更新処理
		CommentEvent entity = repository.findById(commentEventId);

		entity.setDeleted(true);

		repository.saveAndFlush(entity);

		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message",
				messageSource.getMessage("comments.delete.flash", new String[] {}, locale));
		return "redirect:/events";
	}
}