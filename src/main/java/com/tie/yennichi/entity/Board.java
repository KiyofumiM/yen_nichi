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
 * 掲示板情報を表すエンティティクラス。
 * boardテーブルと対応
 */
@Entity
@Table(name = "board")
@Data
public class Board extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "board_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
    private Long userId;
	
	@ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
	
	@Column(nullable = false, length = 30)
	private String title;
	
	@Column(nullable = false, length = 1000)
	private String description;
	
    @Column(nullable = false)
    private boolean deleted;
    
    // 「いいね！」情報と紐づけ
    @OneToMany
    @JoinColumn(name = "boardId", insertable = false, updatable = false)
    private List<GoodBoard> goodList;
    
    // 投稿に対するコメントを紐づけ
    @OneToMany
    @JoinColumn(name = "boardId", insertable = false, updatable = false)
    private List<CommentBoard> commentList;
}
