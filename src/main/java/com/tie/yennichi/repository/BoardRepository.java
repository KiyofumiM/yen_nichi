package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.Board;

/**
 * Boardエンティティの永続化と取得を行うリポジトリクラス。
 * データベースアクセスと対応しています。
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

	Iterable<Board> findByDeletedFalseOrderByUpdatedAtDesc();
	
	Board findById(long id);
}
