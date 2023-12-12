package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.CommentLearning;

@Repository
public interface CommentLearningRepository extends JpaRepository<CommentLearning, Long> {
}