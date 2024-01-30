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

import com.tie.yennichi.entity.GoodCommentLearning;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.repository.GoodCommentLearningRepository;

/**
* 言葉を広げるで投稿されたコメントに対する「いいね！」に関係するcontroller群
*/
@Controller
public class GoodCommentLearnigsController {
	@Autowired
    private MessageSource messageSource;

    @Autowired
    private GoodCommentLearningRepository repository;
    
	/**
	 * コメントに対して「いいね！」をする
	 * @param  Principal, comment_learning_id, RedirectAttributes, Locale
	 * @return redirect:/learning
	 * @throws なし
	 */
    @RequestMapping(value = "/good_comment_learning", method = RequestMethod.POST)
    public String create(Principal principal, @RequestParam("comment_learning_id") long commentLearningId, RedirectAttributes redirAttrs,
            Locale locale) {
    	
        Authentication authentication = (Authentication) principal;
        
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        
        List<GoodCommentLearning> results = repository.findByUserIdAndCommentLearningId(userId, commentLearningId);
        
        if (results.size() == 0) {
            GoodCommentLearning entity = new GoodCommentLearning();
            entity.setUserId(userId);
            entity.setCommentLearningId(commentLearningId);
            repository.saveAndFlush(entity);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message",
                    messageSource.getMessage("goods.create.flash", new String[] {}, locale));
        }
        return "redirect:/learning";
    }

	/**
	 * コメントに対して「いいね！」をやめる
	 * @param  Principal, comment_learning_id, RedirectAttributes, Locale
	 * @return redirect:/learning
	 * @throws なし
	 */
    @RequestMapping(value = "/good_comment_learning", method = RequestMethod.DELETE)
    @Transactional
    public String destroy(Principal principal, @RequestParam("comment_learning_id") long commentLearningId, RedirectAttributes redirAttrs,
            Locale locale) {

        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<GoodCommentLearning> results = repository.findByUserIdAndCommentLearningId(userId, commentLearningId);
        if (results.size() == 1) {
            repository.deleteByUserIdAndCommentLearningId(user.getUserId(), commentLearningId);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message",
                    messageSource.getMessage("goods.destroy.flash", new String[] {}, locale));
        }
        return "redirect:/learning";
    }
}
