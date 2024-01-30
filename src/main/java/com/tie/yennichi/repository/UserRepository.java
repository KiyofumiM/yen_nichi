package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.User;

/**
 * Userエンティティの永続化と取得を行うリポジトリクラス。
 * データベースアクセスと対応しています。
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);
    
   
}