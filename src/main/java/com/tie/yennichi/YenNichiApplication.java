package com.tie.yennichi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Spring Bootアプリケーションのエントリーポイントとなるクラス
 */
@SpringBootApplication
public class YenNichiApplication extends SpringBootServletInitializer {

	// アプリケーションの実行を開始するmethod
	public static void main(String[] args) {
		SpringApplication.run(YenNichiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(YenNichiApplication.class);
	}
}
