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
 * イベント情報に対するコメントを表すエンティティクラス。
 * comment_eventテーブルと対応
 */
@Entity
@Table(name = "comment_event")
@Data
public class CommentEvent extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "comment_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long eventId;
	
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
	
	// コメントに対する「いいね！」と紐づけ
	@OneToMany
	@JoinColumn(name = "commentEventId", insertable = false, updatable = false)
	private List<GoodCommentEvent> goodCommentList;
}
