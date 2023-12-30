package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.Board;
import com.tie.yennichi.entity.Learning;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

	Iterable<Board> findAllByOrderByUpdatedAtDesc();
	
	Board findById(long id);
}
