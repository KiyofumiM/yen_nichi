package com.tie.yennichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* 使い方へのリンク関係の処理用controller群
*/
@Controller
public class InformationsController {
	/**
	* ログイン前の使い方説明画面にリンクする
	* @param  なし
	* @return ページアドレス /information/logging_out
	* @throws なし
	*/
    @RequestMapping("/information/logging_out")
    public String index() {
        return "information/logging_out";
    }
    
	/**
	* ログイン後の使い方説明画面にリンクする
	* @param  なし
	* @return ページアドレス /information/logging_in
	* @throws なし
	*/
    @RequestMapping("/information/logging_in")
    public String content() {
        return "information/logging_in";
    }
}
