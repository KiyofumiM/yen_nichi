package com.tie.yennichi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.GoodBoard;

/**
 * GoodBoardエンティティの永続化と取得を行うリポジトリクラス。
 * データベースアクセスと対応しています。
 */
@Repository
public interface GoodBoardRepository extends JpaRepository<GoodBoard, Long>{

    public Optional<GoodBoard> findById(String id);

    public List<GoodBoard> findByUserIdOrderByUpdatedAtDesc(Long userId);

    public List<GoodBoard> findByUserIdAndBoardId(Long userId, Long boardId);

    public List<GoodBoard> deleteByUserIdAndBoardId(long userId, long BoardId);
}