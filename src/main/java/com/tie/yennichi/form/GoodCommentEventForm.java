package com.tie.yennichi.form;

import lombok.Data;

/**
 * 「イベント情報のシェア」で投稿されたコメント対する「いいね！」情報を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class GoodCommentEventForm {

    private Long userId;

    private Long commentEventId;

    private CommentEventForm commentEvent;
}
