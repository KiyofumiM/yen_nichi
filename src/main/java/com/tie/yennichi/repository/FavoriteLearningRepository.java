package com.tie.yennichi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.FavoriteLearning;

@Repository
public interface FavoriteLearningRepository extends JpaRepository<FavoriteLearning, Long> {

    public Optional<FavoriteLearning> findById(String id);

    public List<FavoriteLearning> findByUserIdOrderByUpdatedAtDesc(Long userId);

    public List<FavoriteLearning> findByUserIdAndLearningId(Long userId, Long learningId);

    public void deleteByUserIdAndLearningId(long userId, long learningId);
}