package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.Learning;

/**
 * learningエンティティの永続化と取得を行うリポジトリクラス。
 * データベースアクセスと対応しています。
 */
@Repository
public interface LearningRepository extends JpaRepository<Learning, Long> {

    Iterable<Learning> findByDeletedFalseOrderByUpdatedAtDesc();
    
    Iterable<Learning> findByDeletedFalse();
    
    Learning findById(long id);
}