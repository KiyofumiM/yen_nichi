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

import com.tie.yennichi.entity.GoodCommentEvent;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.repository.GoodCommentEventRepository;

/**
* イベント情報のシェアで投稿されたコメントに対する「いいね！」に関係するcontroller群
*/
@Controller
public class GoodCommentEventsController {
	@Autowired
    private MessageSource messageSource;

    @Autowired
    private GoodCommentEventRepository repository;
    
	/**
	 * コメントに対して「いいね！」をする
	 * @param  Principal, comment_event_id, RedirectAttributes, Locale
	 * @return redirect:/learning
	 * @throws なし
	 */
    @RequestMapping(value = "/good_comment_event", method = RequestMethod.POST)
    public String create(Principal principal, @RequestParam("comment_event_id") long commentEventId, RedirectAttributes redirAttrs,
            Locale locale) {
    	
        Authentication authentication = (Authentication) principal;
        
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        
        List<GoodCommentEvent> results = repository.findByUserIdAndCommentEventId(userId, commentEventId);
        
        if (results.size() == 0) {
            GoodCommentEvent entity = new GoodCommentEvent();
            entity.setUserId(userId);
            entity.setCommentEventId(commentEventId);
            repository.saveAndFlush(entity);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message",
                    messageSource.getMessage("goods.create.flash", new String[] {}, locale));
        }
        return "redirect:/events";
    }

	/**
	 * コメントに対して「いいね！」をやめる
	 * @param  Principal, comment_event_id, RedirectAttributes, Locale
	 * @return redirect:/learning
	 * @throws なし
	 */
    @RequestMapping(value = "/good_comment_event", method = RequestMethod.DELETE)
    @Transactional
    public String destroy(Principal principal, @RequestParam("comment_event_id") long commentEventId, RedirectAttributes redirAttrs,
            Locale locale) {

        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<GoodCommentEvent> results = repository.findByUserIdAndCommentEventId(userId, commentEventId);
        if (results.size() == 1) {
            repository.deleteByUserIdAndCommentEventId(user.getUserId(), commentEventId);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message",
                    messageSource.getMessage("goods.destroy.flash", new String[] {}, locale));
        }
        return "redirect:/events";
    }
}
