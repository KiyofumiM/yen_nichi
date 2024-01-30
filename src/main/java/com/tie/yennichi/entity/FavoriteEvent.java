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
 * 投稿されたイベント情報に対する「おきにいり！」を表すエンティティクラス。
 * favorite_eventテーブルと対応
 */
@Entity
@Table(name = "favorite_event")
@Data
public class FavoriteEvent extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "favorite_learning_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long eventId;

    // イベント情報と紐づけ
    @ManyToOne
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    private Event event;
}