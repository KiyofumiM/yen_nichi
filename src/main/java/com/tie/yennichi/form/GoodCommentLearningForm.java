package com.tie.yennichi.form;

import lombok.Data;

/**
 * 「言葉を広げる」で投稿されたコメント対する「いいね！」情報を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class GoodCommentLearningForm {

    private Long userId;

    private Long commentLearningId;

    private CommentLearningForm commentLearning;
}
