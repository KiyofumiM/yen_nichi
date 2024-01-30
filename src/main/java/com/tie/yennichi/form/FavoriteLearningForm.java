package com.tie.yennichi.form;

import lombok.Data;

/**
 * 投稿された言葉に対する「お気に入り！」情報を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class FavoriteLearningForm {

    private Long userId;

    private Long learningId;

    private LearningForm learning;
}
