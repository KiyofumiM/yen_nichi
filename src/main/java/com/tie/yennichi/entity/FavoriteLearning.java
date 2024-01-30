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
 * 投稿された言葉に対する「お気に入り！」を表すエンティティクラス。
 * favorite_learningテーブルと対応
 */
@Entity
@Table(name = "favorite_learning")
@Data
public class FavoriteLearning extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "favorite_learning_id_seq")
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