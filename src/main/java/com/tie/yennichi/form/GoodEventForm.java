package com.tie.yennichi.form;

import lombok.Data;

/**
 * 投稿されたイベント情報に対する「いいね！」を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class GoodEventForm {

    private Long userId;

    private Long EventId;
    
    private EventForm event;
}
