package com.tie.yennichi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
* Spring Bootアプリケーションのエントリーポイントとなるクラス
*/
@SpringBootApplication
public class YenNichiApplication {

	// アプリケーションの実行を開始するmethod
	public static void main(String[] args) {
		SpringApplication.run(YenNichiApplication.class, args);
	}

}
