package com.tie.yennichi.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CommentEventForm {

	private Long id;

    private Long eventId;
    
    private UserForm user;

    @NotEmpty
    @Size(max = 1000)
    private String description;
    
    private boolean deleted;
    
    private List<GoodCommentEventForm> goodComments;

    private GoodCommentEventForm goodComment;

}