package com.tie.yennichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

/**
* session関係の処理用controller群
*/
@Controller
public class SessionsController {

	@Autowired
	private MessageSource messageSource;
	
    /**
     * ログイン画面にリンク
     * @param なし
     * @return /sessions/new
     * @throws なし
     */
    @GetMapping(path = "/login")
    public String index() {
        return "sessions/new";
    }

    /**
     * ログインエラー処理
     * @param Model。locale
     * @return /sessions/new
     * @throws なし
     */
    @GetMapping(path = "/login-failure")
    public String loginFailure(Model model, Locale locale) {
        model.addAttribute("hasMessage", true);
        model.addAttribute("class", "alert-danger");
        model.addAttribute("message", messageSource.getMessage("sessions.loginFailure.flash", new String[] {}, locale));

        return "sessions/new";
    }

    /**
     * ログアウト処理
     * @param Model。locale
     * @return /pages/index
     * @throws なし
     */
    @GetMapping(path = "/logout-complete")
    public String logoutComplete(Model model, Locale locale) {
        model.addAttribute("hasMessage", true);
        model.addAttribute("class", "alert-info");
        model.addAttribute("message", messageSource.getMessage("sessions.logoutComplete.flash", new String[] {}, locale));

        return "pages/index";
    }
}