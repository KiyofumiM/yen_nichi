package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.Event;

/**
 * eventエンティティの永続化と取得を行うリポジトリクラス。
 * データベースアクセスと対応しています。
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Iterable<Event> findByDeletedFalseOrderByUpdatedAtDesc();
    
    Iterable<Event> findByDeletedFalse();
    
    Event findById(long id);
}
