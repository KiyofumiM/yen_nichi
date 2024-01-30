package com.tie.yennichi.form;

import lombok.Data;

/**
 * 投稿された言葉に対する「いいね！」情報を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class GoodLearningForm {

    private Long userId;

    private Long learningId;

    private LearningForm learning;
}
