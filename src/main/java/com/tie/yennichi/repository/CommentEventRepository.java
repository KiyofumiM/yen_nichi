package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.CommentEvent;

/**
 * CommentEventエンティティの永続化と取得を行うリポジトリクラス。
 * データベースアクセスと対応しています。
 */
@Repository
public interface CommentEventRepository extends JpaRepository<CommentEvent, Long> {
	
	CommentEvent findById(long commentEventId);
	
	Iterable<CommentEvent> findByEventIdAndDeletedFalseOrderByUpdatedAtDesc(long eventId);
}