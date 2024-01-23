package com.tie.yennichi.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.tie.yennichi.entity.CommentLearning;
import com.tie.yennichi.validation.constraints.ImageByte;
import com.tie.yennichi.validation.constraints.ImageNotEmpty;

import lombok.Data;

@Data
public class LearningForm {

    private Long id;

    private Long userId;

    @ImageNotEmpty
    @ImageByte(max = 2000000)
    private MultipartFile image;

    private String imageData;

    private String path;

    @NotEmpty
    @Size(max = 20)
    private String title;
    
    @Size(max = 1000)
    private String description;
    
    private boolean deleted;
    
    private UserForm user;

    private List<GoodLearningForm> goods;
    
    private GoodLearningForm good;
    
    private List<FavoriteLearningForm> favorites;
    
    private FavoriteLearningForm favorite;
    
    private List<CommentLearningForm> comments;
    
    private List<GoodCommentLearningForm> goodComments;
    
    private GoodCommentLearningForm goodComment;
    
    public List<CommentLearningForm> getValidComments() {

    	List<CommentLearningForm> src = comments;
    	List<CommentLearningForm> ret = new ArrayList<CommentLearningForm>();
    	for (CommentLearningForm item : src) {
    		if (!item.isDeleted()) {
    			ret.add(item);
    		}
    	}
    	return ret;
    }

}