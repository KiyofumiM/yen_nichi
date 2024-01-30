package com.tie.yennichi.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 掲示板に投稿された内容を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
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

    // 投稿に対する「いいね！」一覧
    private List<GoodBoardForm> goodList;
    
    // 各「いいね！」情報
    private GoodBoardForm good;
    
    // 投稿に対するコメント一覧
    private List<CommentBoardForm> commentList;
    
    // 各コメント情報
    private List<GoodCommentBoardForm> goodCommentList;
    
    // コメントに対する「いいね！」
    private GoodCommentBoardForm goodComment;
    
    /**
     * 論理削除されていないコメントを表示する
     * @return 論理削除されていないコメント情報
     */
    public List<CommentBoardForm> getValidComments() {

    	List<CommentBoardForm> listComment = commentList;
    	List<CommentBoardForm> retList = new ArrayList<CommentBoardForm>();
    	for (CommentBoardForm item : listComment) {
    		if (!item.isDeleted()) {
    			retList.add(item);
    		}
    	}
    	return retList;
    }
}
