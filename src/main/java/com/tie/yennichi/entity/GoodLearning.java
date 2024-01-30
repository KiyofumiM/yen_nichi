package com.tie.yennichi.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * 投稿された言葉に対する「いいね！」を表すエンティティクラス。
 * good_learningテーブルと対応
 */
@Entity
@Table(name = "good_learning")
@Data
public class GoodLearning extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "good_learning_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long learningId;

    // learningと紐づけ
    @ManyToOne
    @JoinColumn(name = "learningId", insertable = false, updatable = false)
    private Learning learning;
}
