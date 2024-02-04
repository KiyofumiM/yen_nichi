package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.CommentBoard;
import com.tie.yennichi.entity.CommentLearning;

/**
 * CommentBoardエンティティの永続化と取得を行うリポジトリクラス。
 * データベースアクセスと対応しています。
 */
@Repository
public interface CommentBoardRepository extends JpaRepository<CommentBoard, Long> {
	
	CommentBoard findById(long commentBoardId);
	
	Iterable<CommentBoard> findByBoardIdAndDeletedFalseOrderByUpdatedAtDesc(long boardId);
}