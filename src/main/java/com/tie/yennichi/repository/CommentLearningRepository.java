package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.CommentLearning;

/**
 * CommentLearningエンティティの永続化と取得を行うリポジトリクラス。
 * データベースアクセスと対応しています。
 */
@Repository
public interface CommentLearningRepository extends JpaRepository<CommentLearning, Long> {

	Iterable<CommentLearning> findByLearningIdAndDeletedFalseOrderByUpdatedAtDesc(long learningId);
	
	CommentLearning findById(long commentLearningId);
}