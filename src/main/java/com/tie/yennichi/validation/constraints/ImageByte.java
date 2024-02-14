package com.tie.yennichi.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

/**
 * 画像のバイトサイズが指定された最大値以下であるかどうかを検証するためのカスタムバリデーションアノテーション
 */
@Documented
@Constraint(validatedBy = ImageByteValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface ImageByte {

	String message() default "{com.tie.yennichi.validation.constraints.ImageSize.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	// 画像の最大バイトサイズを指定
	int max();
}