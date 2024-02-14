package com.tie.yennichi.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

/**
* ImageNotEmptyアノテーションで指定された条件に基づいて、MultipartFile（Spring FrameworkのMultipartFileはファイルアップロード用のオブジェクト）が空でないかどうかを検証するためのバリデータ
*/
public class ImageNotEmptyValidator implements ConstraintValidator<ImageNotEmpty, MultipartFile> {

    @Override
    public void initialize(ImageNotEmpty annotation) {
    }

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext context) {
        if (image.isEmpty()) {
            return false;
        }
        return true;
    }
}