package com.tie.yennichi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.GoodLearning;

@Repository
public interface GoodLearningRepository extends JpaRepository<GoodLearning, Long>{

    public Optional<GoodLearning> findById(String id);

    public List<GoodLearning> findByUserIdOrderByUpdatedAtDesc(Long userId);

    public List<GoodLearning> findByUserIdAndLearningId(Long userId, Long learningId);

    public void deleteByUserIdAndLearningId(long userId, long learningId);
}
