package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);
}