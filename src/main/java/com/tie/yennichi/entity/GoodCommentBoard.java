package com.tie.yennichi.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * 掲示板にて投稿されたコメントに対する「いいね！」を表すエンティティクラス。
 * god_comment_boardテーブルと対応
 */
@Entity
@Table(name = "good_comment_board")
@Data
public class GoodCommentBoard extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "good_comment_board_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long commentBoardId;

}
