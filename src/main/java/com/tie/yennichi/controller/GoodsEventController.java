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

import com.tie.yennichi.entity.GoodEvent;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.repository.GoodEventRepository;

/**
* イベント情報のシェアで投稿された内容に対する「いいね！」に関係するcontroller群
*/
@Controller
public class GoodsEventController {
	@Autowired
    private MessageSource messageSource;

    @Autowired
    private GoodEventRepository repository;

    @Autowired
    private EventsController eventsController;

	/**
	 * 投稿内容に対して「いいね！」をする
	 * @param  Principal, event_id, RedirectAttributes, Locale
	 * @return redirect:/learning
	 * @throws なし
	 */
    @RequestMapping(value = "/good_event", method = RequestMethod.POST)
    public String create(Principal principal, @RequestParam("event_id") long eventId, RedirectAttributes redirAttrs,
            Locale locale) {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<GoodEvent> results = repository.findByUserIdAndEventId(userId, eventId);
        if (results.size() == 0) {
            GoodEvent entity = new GoodEvent();
            entity.setUserId(userId);
            entity.setEventId(eventId);
            repository.saveAndFlush(entity);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", messageSource.getMessage("goods.create.flash", new String[] {}, locale));
        }

        return "redirect:/events";
    }

	/**
	 * 投稿内容に対して「いいね！」をやめる
	 * @param  Principal, event_id, RedirectAttributes, Locale
	 * @return redirect:/learning
	 * @throws なし
	 */
    @RequestMapping(value = "/good_event", method = RequestMethod.DELETE)
    @Transactional
    public String destroy(Principal principal, @RequestParam("event_id") long eventId, RedirectAttributes redirAttrs,
            Locale locale) {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        List<GoodEvent> results = repository.findByUserIdAndEventId(userId, eventId);;
        
        if (results.size() == 1) {
            repository.deleteByUserIdAndEventId(user.getUserId(), eventId);

            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", messageSource.getMessage("goods.destroy.flash", new String[] {}, locale));
        }
        return "redirect:/events";
    }

}
