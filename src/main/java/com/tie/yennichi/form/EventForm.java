package com.tie.yennichi.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.tie.yennichi.validation.constraints.ImageByte;
import com.tie.yennichi.validation.constraints.ImageNotEmpty;

import lombok.Data;

@Data
public class EventForm {

    private Long id;

    private Long userId;

    @ImageNotEmpty
    @ImageByte(max = 2000000)
    private MultipartFile image;

    private String imageData;

    private String path;

    @NotEmpty
    private String event_at;
    
    @NotEmpty
    @Size(max = 20)
    private String title;
    
    @Size(max = 1000)
    private String description;
    
    private boolean deleted;

    private UserForm user;

    private List<GoodEventForm> goods;
    
    private GoodEventForm good;
    
    private List<FavoriteEventForm> favorites;
    
    private FavoriteEventForm favorite;
    
    private List<CommentEventForm> comments;
    
    private List<GoodCommentEventForm> goodComments;
    
    private GoodCommentEventForm goodComment;
    
    public List<CommentEventForm> getValidComments() {

    	List<CommentEventForm> src = comments;
    	List<CommentEventForm> ret = new ArrayList<CommentEventForm>();
    	for (CommentEventForm item : src) {
    		if (!item.isDeleted()) {
    			ret.add(item);
    		}
    	}
    	return ret;
    }
}

