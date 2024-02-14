package com.tie.yennichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* ページリンク関係の処理用controller群
*/
@Controller
public class PagesController {

	/**
	* トップ画面にリンクする
	* @param  なし
	* @return ページアドレス /pages/index
	* @throws なし
	*/
    @RequestMapping("/")
    public String index() {
        return "pages/index";
    }
    
	/**
	* コンテンツ一覧画面にリンクする
	* @param  なし
	* @return ページアドレス /pages/contents
	* @throws なし
	*/
    @RequestMapping("/contents")
    public String content() {
        return "pages/contents";
    }
}
