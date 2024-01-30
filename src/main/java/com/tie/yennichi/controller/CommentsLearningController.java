package com.tie.yennichi.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.entity.CommentLearning;
import com.tie.yennichi.form.CommentLearningForm;
import com.tie.yennichi.repository.CommentLearningRepository;

/**
* 言葉を広げるで投稿するコメントの処理用controller群
*/
@Controller
public class CommentsLearningController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentLearningRepository repository;

	/**
	* 新規投稿画面にリンクする
	* @param  learningId, Model
	* @return ページアドレス /comments_learning/new
	* @throws なし
	*/
    @RequestMapping(value = "/learning/{learningId}/comments_learning/new")
    public String newComment(@PathVariable("learningId") long learningId, Model model) {
        CommentLearningForm form = new CommentLearningForm();
        form.setLearningId(learningId);
        model.addAttribute("form", form);
        return "comments_learning/new";
    }
    
	/**
	* 投稿処理
	* @param  learningId, form : CommentLearningForm,
            BindingResult, Principal, Model, RedirectAttributes, Locale
	* @return redirect:/learning
	* @throws なし
	*/
    @RequestMapping(value = "/learning/{learningId}/comment_learning")
    public String create(@PathVariable("learningId") long learningId, @Validated @ModelAttribute("form") CommentLearningForm form,
            BindingResult result, Principal principal, Model model, RedirectAttributes redirAttrs, Locale locale) {
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", messageSource.getMessage("comments.create.flash.1", new String[] {}, locale));
            return "comments_learning/new";
        }
        
        CommentLearning entity = modelMapper.map(form, CommentLearning.class);
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        entity.setLearningId(learningId);
        entity.setUserId(user.getUserId());
        entity.setDeleted(false);
        repository.saveAndFlush(entity);

        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("comments.create.flash.2", new String[] {}, locale));

        return "redirect:/learning";
    }
    
	/**
	 * 参照した投稿内容の詳細を取得して表示
	 * @param  Principal, comment_learning_id, Model
	 * @return ページアドレス /comments_learning/edit
	 * @throws なし
	 */
	@RequestMapping(value = "/comment_learning/edit", method = RequestMethod.GET)
	public String edite(Principal principal, @RequestParam("comment_learning_id") long commentLearningId, Model model) {
		CommentLearning entity = repository.findById(commentLearningId);

		CommentLearningForm commentLearningForm = new CommentLearningForm();
		commentLearningForm.setId(entity.getId());

		commentLearningForm.setDescription(entity.getDescription());
		model.addAttribute("form", commentLearningForm);
		return "comments_learning/edit";
	}
    
	/**
	* 投稿内容の更新処理
	* @param  rincipal, form : CommentLearningForm,
			BindingResult, Model, Locale, RedirectAttributes, id
	* @return redirect:/learning
	* @throws IOException
	*/
	@RequestMapping(value = "/comment_learning/update", method = RequestMethod.POST)
	public String update(Principal principal, @Validated @ModelAttribute("form") CommentLearningForm form,
			BindingResult result, Model model, Locale locale, RedirectAttributes redirAttrs, @RequestParam("id") long commentLearningId 
			) throws IOException {

		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", messageSource.getMessage("comments.edit.flash.1", new String[] {}, locale));
			return "comments_learning/edit";
		}

		// 更新処理
		CommentLearning entity = repository.findById(commentLearningId);

		String description = form.getDescription();

		entity.setDescription(description);

		repository.saveAndFlush(entity);

		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", messageSource.getMessage("comments.edit.flash.2", new String[] {}, locale));
		return "redirect:/learning";
	}

	/**
	* 投稿内容の論理削除処理
	* @param  rincipal, form : CommentLearningForm,
			BindingResult, Model, Locale, RedirectAttributes, id
	* @return redirect:/learning
	* @throws IOException
	*/
	@RequestMapping(value = "/comment_learning/delete", method = RequestMethod.GET)
	public String delete(Principal principal, Model model, Locale locale, RedirectAttributes redirAttrs,
			@RequestParam("comment_learning_id") long commentLearningId) throws IOException {

		// 更新処理
		CommentLearning entity = repository.findById(commentLearningId);

		entity.setDeleted(true);

		repository.saveAndFlush(entity);

		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", messageSource.getMessage("comments.delete.flash", new String[] {}, locale));
		return "redirect:/learning";
	}
}
