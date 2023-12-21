package com.tie.yennichi.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CommentBoardForm {

	private Long id;

    private Long boardId;
    
    private UserForm user;

    @NotEmpty
    @Size(max = 1000)
    private String description;

}