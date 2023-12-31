package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.Learning;
import com.tie.yennichi.entity.User;

@Repository
public interface LearningRepository extends JpaRepository<Learning, Long> {

    Iterable<Learning> findByDeletedFalseOrderByUpdatedAtDesc();
    
    Learning findById(long id);
}