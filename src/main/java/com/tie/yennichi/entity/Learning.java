package com.tie.yennichi.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "learning")
@Data
public class Learning extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "learning_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false, length = 20)
    private String title;
    
    @Column(nullable = true, length = 1000)
	private String description;

    @Column(nullable = false)
    private boolean deleted;
    
    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
    
    @OneToMany
    @JoinColumn(name = "learningId", insertable = false, updatable = false)
    private List<GoodLearning> goods;
    
    @OneToMany
    @JoinColumn(name = "learningId", insertable = false, updatable = false)
    private List<FavoriteLearning> favorites;
    
    @OneToMany
    @JoinColumn(name = "learningId", insertable = false, updatable = false)
    private List<CommentLearning> comments;
    
}