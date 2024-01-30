package com.tie.yennichi.form;

import lombok.Data;

/**
 * イベントカレンダーで使う情報を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class ViewEventFrom {

    private String title;

    private String start;

    private String end;

    private String url;
}
