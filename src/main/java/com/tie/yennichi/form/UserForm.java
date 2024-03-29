package com.tie.yennichi.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.tie.yennichi.validation.constraints.PasswordEquals;

import lombok.Data;

/**
 * ユーザー情報を受け取るためのフォームクラス
 * 入力フォームと対応しています。
 */
@Data
@PasswordEquals
public class UserForm {

    @NotEmpty
    @Size(max = 100)
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(max = 20)
    private String password;

    @NotEmpty
    @Size(max = 20)
    private String passwordConfirmation;

    
    private Long Id;
}