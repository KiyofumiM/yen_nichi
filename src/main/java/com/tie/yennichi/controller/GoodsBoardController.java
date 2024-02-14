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

import com.tie.yennichi.entity.GoodBoard;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.repository.GoodBoardRepository;

/**
* 掲示板で投稿された内容に対する「いいね！」に関係するcontroller群
*/
@Controller
public class GoodsBoardController {
	@Autowired
    private MessageSource messageSource;

    @Autowired
    private GoodBoardRepository repository;

    /**
	 * 投稿内容に対して「いいね！」をする
	 * @param  Principal, board_id, RedirectAttributes, Locale
	 * @return redirect:/learning
	 * @throws なし
	 */
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
            redirAttrs.addFlashAttribute("message", messageSource.getMessage("goods.create.flash", new String[] {}, locale));
        }

        return "redirect:/board";
    }

	/**
	 * 投稿内容に対して「いいね！」をやめる
	 * @param  Principal, board_id, RedirectAttributes, Locale
	 * @return redirect:/learning
	 * @throws なし
	 */
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
            redirAttrs.addFlashAttribute("message", messageSource.getMessage("goods.destroy.flash", new String[] {}, locale));
        }
        return "redirect:/board";
    }

}
