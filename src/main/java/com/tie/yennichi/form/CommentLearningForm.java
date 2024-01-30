package com.tie.yennichi.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import java.util.List;

/**
 * 投稿された言葉に対するコメント情報を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class CommentLearningForm {

	private Long id;

    private Long learningId;
    
    private UserForm user;

    @NotEmpty
    @Size(max = 1000)
    private String description;
    
    private boolean deleted;
    
    // 投稿されたコメントに対する「いいね！」一覧
    private List<GoodCommentLearningForm> goodCommentList;

    // 投稿されたコメントに対する各「いいね！」情報
    private GoodCommentLearningForm goodComment;
  
}