package com.tie.yennichi.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.tie.yennichi.validation.constraints.ImageByte;
import com.tie.yennichi.validation.constraints.ImageNotEmpty;

import lombok.Data;

/**
 * 「登録されたイベント情報」を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
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

    // 「いいね！」一覧
    private List<GoodEventForm> goodList;
    
    // 各「いいね！」情報
    private GoodEventForm good;
    
    // 「お気に入り！」一覧
    private List<FavoriteEventForm> favoriteList;
    
    // 各「お気に入り！」情報
    private FavoriteEventForm favorite;
    
    // コメント一覧
    private List<CommentEventForm> commentList;
    
    // コメントに対する「いいね！」一覧
    private List<GoodCommentEventForm> goodCommentList;
    
    // 各コメントに対する「いいね！」情報
    private GoodCommentEventForm goodComment;
    
    /**
     * 論理削除されていないコメントを表示する
     * @return 論理削除されていないコメント情報
     */
    public List<CommentEventForm> getValidComments() {

    	List<CommentEventForm> listComment = commentList;
    	List<CommentEventForm> retList = new ArrayList<CommentEventForm>();
    	for (CommentEventForm item : listComment) {
    		if (!item.isDeleted()) {
    			retList.add(item);
    		}
    	}
    	return retList;
    }
}

