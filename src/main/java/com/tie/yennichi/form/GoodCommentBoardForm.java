package com.tie.yennichi.form;

import lombok.Data;

/**
 * 「掲示板」で投稿されたコメント対する「いいね！」情報を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class GoodCommentBoardForm {

    private Long userId;

    private Long commentBoardId;

    private CommentBoardForm commentBoard;
}
