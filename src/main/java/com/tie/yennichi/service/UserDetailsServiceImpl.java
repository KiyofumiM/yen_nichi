package com.tie.yennichi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tie.yennichi.entity.User;
import com.tie.yennichi.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ユーザ認証をするためのサービスクラス
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	protected static Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	// UserDetailsService インターフェースの実装メソッド
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		log.debug("username={}", username);

		if (username == null || "".equals(username)) {
			throw new UsernameNotFoundException("Username is empty");
		}
		// ユーザーネームをもとにデータベースからユーザーエンティティを取得
		User entity = repository.findByUsername(username);

		// 取得したユーザーエンティティを UserDetails インターフェースを実装したオブジェクトに変換して返す
		return entity;
	}

	public UserRepository getRepository() {
		return repository;
	}

	public void setRepository(UserRepository repository) {
		this.repository = repository;
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		UserDetailsServiceImpl.log = log;
	}

}