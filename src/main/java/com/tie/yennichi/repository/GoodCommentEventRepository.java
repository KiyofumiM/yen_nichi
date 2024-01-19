package com.tie.yennichi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.GoodCommentEvent;

@Repository
public interface GoodCommentEventRepository  extends JpaRepository<GoodCommentEvent, Long>{

	public Optional<GoodCommentEvent> findById(String id);

    public List<GoodCommentEvent> findByUserIdOrderByUpdatedAtDesc(Long userId);

    public List<GoodCommentEvent> findByUserIdAndCommentEventId(Long userId, Long commentEventId);

    public void deleteByUserIdAndCommentEventId(long userId, long commentEventId);
}
