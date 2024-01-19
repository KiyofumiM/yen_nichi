package com.tie.yennichi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.GoodCommentLearning;

@Repository
public interface GoodCommentLearningRepository  extends JpaRepository<GoodCommentLearning, Long>{

	public Optional<GoodCommentLearning> findById(String id);

    public List<GoodCommentLearning> findByUserIdOrderByUpdatedAtDesc(Long userId);

    public List<GoodCommentLearning> findByUserIdAndCommentLearningId(Long userId, Long commentLearningId);

    public void deleteByUserIdAndCommentLearningId(long userId, long commentLearningId);
}
