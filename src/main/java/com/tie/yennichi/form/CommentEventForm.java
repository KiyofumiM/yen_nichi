package com.tie.yennichi.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 投稿されたイベントに対するコメント情報を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class CommentEventForm {

	private Long id;

    private Long eventId;
    
    private UserForm user;

    @NotEmpty
    @Size(max = 1000)
    private String description;
    
    private boolean deleted;
    
    // 投稿されたコメントに対する「いいね！」一覧
    private List<GoodCommentEventForm> goodCommentList;

    // 投稿されたコメントに対する各「いいね！」情報
    private GoodCommentEventForm goodComment;

}