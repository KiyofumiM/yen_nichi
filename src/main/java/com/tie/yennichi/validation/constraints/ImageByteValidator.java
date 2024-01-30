package com.tie.yennichi.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

/**
 * ImageByteアノテーションで指定された条件に基づいて、MultipartFile（Spring FrameworkのMultipartFileはファイルアップロード用のオブジェクト）が許容されるバイトサイズ以下であるかどうかを検証するためのバリデータ
 */
public class ImageByteValidator implements ConstraintValidator<ImageByte, MultipartFile> {

	int max;

	@Override
	public void initialize(ImageByte annotation) {
		this.max = annotation.max();
	}

	// 実際の検証処理
	@Override
	public boolean isValid(MultipartFile image, ConstraintValidatorContext context) {
		// 画像のバイトサイズが許容される最大バイトサイズ以下であればtrueを返す
		if (image.getSize() > this.max) {
			return false;
		}
		return true;
	}
}