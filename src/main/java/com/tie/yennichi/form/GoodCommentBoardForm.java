package com.tie.yennichi.form;

import lombok.Data;

@Data
public class GoodCommentBoardForm {

    private Long userId;

    private Long commentBoardId;

    private CommentBoardForm commentBoard;
}
