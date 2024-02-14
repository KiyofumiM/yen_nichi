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
 * 「登録された言葉」を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
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

    // 「いいね！」一覧
    private List<GoodLearningForm> goodList;
    
    // 各「いいね！」情報
    private GoodLearningForm good;
    
    // 「お気に入り！」一覧
    private List<FavoriteLearningForm> favoriteList;
    
    // 各「お気に入り！」情報
    private FavoriteLearningForm favorite;
    
    // コメント一覧
    private List<CommentLearningForm> commentList;
    
    // 各コメント情報
    private List<GoodCommentLearningForm> goodCommentList;
    
    // コメントに対する「いいね！」フォーム
    private GoodCommentLearningForm goodComment;
     
    /**
     * 論理削除されていないコメントを表示する
     * @return 論理削除されていないコメント情報
     */
    public List<CommentLearningForm> getValidComments() {

    	List<CommentLearningForm> listComent = commentList;
    	List<CommentLearningForm> retList = new ArrayList<CommentLearningForm>();
    	for (CommentLearningForm item : listComent) {
    		if (!item.isDeleted()) {
    			retList.add(item);
    		}
    	}
    	return retList;
    }

}