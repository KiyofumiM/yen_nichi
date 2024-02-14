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
 * イベント情報のシェアにて投稿されたコメントに対する「いいね！」を表すエンティティクラス。
 * good_comment_eventテーブルと対応
 */
@Entity
@Table(name = "good_comment_event")
@Data
public class GoodCommentEvent extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "good_comment_event_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long commentEventId;

}
