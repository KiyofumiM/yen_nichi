package com.tie.yennichi.form;

import lombok.Data;

@Data
public class GoodCommentLearningForm {

    private Long userId;

    private Long commentLearningId;

    private CommentLearningForm commentLearning;
}
