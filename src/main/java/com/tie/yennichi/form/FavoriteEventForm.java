package com.tie.yennichi.form;

import lombok.Data;

/**
 * 投稿されたイベントに対する「お気に入り！」情報を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class FavoriteEventForm {

    private Long userId;

    private Long eventId;

    private EventForm event;
}
