package com.tie.yennichi.form;

import lombok.Data;

@Data
public class GoodCommentEventForm {

    private Long userId;

    private Long commentEventId;

    private CommentEventForm commentEvent;
}
