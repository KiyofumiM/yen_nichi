package com.tie.yennichi.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class BoardForm {

	private Long id;
    
    private UserForm user;
    
    @NotEmpty
    @Size(max = 30)
    private String title;
    
    @NotEmpty
    @Size(max = 1000)
    private String description;
    
    private boolean deleted;

    private List<GoodBoardForm> goods;
    
    private GoodBoardForm good;
    
    private List<CommentBoardForm> comments;
    
    private List<GoodCommentBoardForm> goodComments;
    
    private GoodCommentBoardForm goodComment;
    
    public List<CommentBoardForm> getValidComments() {

    	List<CommentBoardForm> src = comments;
    	List<CommentBoardForm> ret = new ArrayList<CommentBoardForm>();
    	for (CommentBoardForm item : src) {
    		if (!item.isDeleted()) {
    			ret.add(item);
    		}
    	}
    	return ret;
    }
}
