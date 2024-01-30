package com.tie.yennichi.form;

import lombok.Data;

/**
 * 掲示板に投稿された内容に対する「いいね！」情報を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class GoodBoardForm {

    private Long userId;

    private Long boardId;
    
    private BoardForm board;
}
