package com.tie.yennichi.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 *お問い合わせ情報を受け取るためのフォームクラス。
 * 入力フォームと対応しています。
 */
@Data
public class ContactForm {

	private Long id;
    
    private UserForm user;
    
    @NotEmpty
    @Size(max = 50)
    private String title;
    
    @NotEmpty
    @Size(max = 1000)
    private String description;
}
