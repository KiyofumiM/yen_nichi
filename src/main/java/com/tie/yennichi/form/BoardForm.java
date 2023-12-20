package com.tie.yennichi.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class BoardForm {

	private Long id;
    
    private UserForm user;
    
    @NotEmpty
    @Size(max = 20)
    private String title;
    
    @NotEmpty
    @Size(max = 1000)
    private String description;

}