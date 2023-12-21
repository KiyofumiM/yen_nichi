package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.CommentBoard;

@Repository
public interface CommentBoardRepository extends JpaRepository<CommentBoard, Long> {
}