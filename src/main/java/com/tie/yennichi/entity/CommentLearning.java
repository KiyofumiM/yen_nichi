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

import java.util.List;
import javax.persistence.OneToMany;

@Entity
@Table(name = "comment_learning")
@Data
public class CommentLearning extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "comment_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long learningId;
	
	@Column(nullable = false)
    private Long userId;
	
	@ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
	
    @Column(nullable = false)
    private boolean deleted;

	@Column(nullable = false, length = 1000)
	private String description;
	
	@OneToMany
	@JoinColumn(name = "commentLearningId", insertable = false, updatable = false)
	private List<GoodCommentLearning> goodComments;
}
