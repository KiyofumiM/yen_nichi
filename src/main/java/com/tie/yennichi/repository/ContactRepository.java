package com.tie.yennichi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tie.yennichi.entity.Contact;

/**
 * Contactエンティティの永続化と取得を行うリポジトリクラス。
 * データベースアクセスと対応しています。
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

}
