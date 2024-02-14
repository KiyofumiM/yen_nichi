package com.tie.yennichi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.GoodCommentBoard;

/**
 * GoodCommentBoardエンティティの永続化と取得を行うリポジトリクラス。
 * データベースアクセスと対応しています。
 */
@Repository
public interface GoodCommentBoardRepository  extends JpaRepository<GoodCommentBoard, Long>{

	public Optional<GoodCommentBoard> findById(String id);

    public List<GoodCommentBoard> findByUserIdOrderByUpdatedAtDesc(Long userId);

    public List<GoodCommentBoard> findByUserIdAndCommentBoardId(Long userId, Long commentBoardId);

    public void deleteByUserIdAndCommentBoardId(long userId, long commentBoardId);
    
}