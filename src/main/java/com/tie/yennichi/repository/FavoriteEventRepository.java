package com.tie.yennichi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.FavoriteEvent;

@Repository
public interface FavoriteEventRepository extends JpaRepository<FavoriteEvent, Long> {

    public Optional<FavoriteEvent> findById(String id);

    public List<FavoriteEvent> findByUserIdOrderByUpdatedAtDesc(Long userId);

    public List<FavoriteEvent> findByUserIdAndEventId(Long userId, Long eventId);

    public void deleteByUserIdAndEventId(long userId, long eventId);
}