package com.tie.yennichi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.GoodEvent;

@Repository
public interface GoodEventRepository extends JpaRepository<GoodEvent, Long>{

    public Optional<GoodEvent> findById(String id);

    public List<GoodEvent> findByUserIdOrderByUpdatedAtDesc(Long userId);

    public List<GoodEvent> findByUserIdAndEventId(Long userId, Long EventId);

    public void deleteByUserIdAndEventId(long userId, long EventId);
}