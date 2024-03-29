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
 * お問い合わせを表すエンティティクラス。
 * contactテーブルと対応
 */
@Entity
@Table(name = "contact")
@Data
public class Contact extends AbstractEntity implements Serializable  {
	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "contact_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String title;
    
    @Column(nullable = true, length = 1000)
	public String description;

    // ユーザ情報と紐づけ
    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
    
}
