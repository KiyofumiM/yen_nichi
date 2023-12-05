package com.tie.yennichi.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.tie.yennichi.validation.constraints.ImageByte;
import com.tie.yennichi.validation.constraints.ImageNotEmpty;

import lombok.Data;

@Data
public class LearningForm {

    private Long id;

    private Long userId;

    @ImageNotEmpty
    @ImageByte(max = 2000000)
    private MultipartFile image;

    private String imageData;

    private String path;

    @NotEmpty
    @Size(max = 1000)
    private String description;
    
    private UserForm user;

}