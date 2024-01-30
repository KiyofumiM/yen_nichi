package com.tie.yennichi.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * 掲示板に投稿された内容に対するコメント情報を表すエンティティクラス。
 * comment_boardテーブルと対応
 */
@Entity
@Table(name = "comment_board")
@Data
public class CommentBoard extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "comment_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long boardId;
	
	@Column(nullable = false)
    private Long userId;
	
	// ユーザ情報と紐づけ
	@ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
	
    @Column(nullable = false)
    private boolean deleted;

	@Column(nullable = false, length = 1000)
	private String description;
	
	// 投稿したコメントに対する「いいね！」と紐づけ
	@OneToMany
	@JoinColumn(name = "commentBoardId", insertable = false, updatable = false)
	private List<GoodCommentBoard> goodCommentList;
}