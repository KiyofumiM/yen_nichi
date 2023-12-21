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

@Entity
@Table(name = "good_board")
@Data
public class GoodBoard  extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "good_board_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long boardId;

    @ManyToOne
    @JoinColumn(name = "boardId", insertable = false, updatable = false)
    private Board board;

}
