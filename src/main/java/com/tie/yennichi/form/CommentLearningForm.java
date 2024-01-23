package com.tie.yennichi.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import java.util.List;

@Data
public class CommentLearningForm {

	private Long id;

    private Long learningId;
    
    private UserForm user;

    @NotEmpty
    @Size(max = 1000)
    private String description;
    
    private boolean deleted;
    
    private List<GoodCommentLearningForm> goodComments;

    private GoodCommentLearningForm goodComment;
  
}