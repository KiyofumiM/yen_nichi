package com.tie.yennichi;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* フォームとエンティティーのプロパティーのプロパティーのアクセサを指定して値をスムーズに移し替えるためのクラス
*/
@Configuration
public class AppConfig {
	
	// ModelMapperのBeanを提供するメソッド
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}