package com.tie.yennichi.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 掲示板に投稿された内容に対するコメント情報を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class CommentBoardForm {

	private Long id;

    private Long boardId;
    
    private UserForm user;

    @NotEmpty
    @Size(max = 1000)
    private String description;
    
    private boolean deleted;

    // コメントに対する「いいね！」一覧
    private List<GoodCommentBoardForm> goodCommentList;

    // コメントに対する各「いいね！」情報
    private GoodCommentBoardForm goodComment;
}