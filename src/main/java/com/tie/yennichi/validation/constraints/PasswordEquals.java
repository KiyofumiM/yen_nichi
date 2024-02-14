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
 * パスワードフィールド同士が等しいかどうかを検証するためクラス
 */
@Documented
@Constraint(validatedBy = PasswordEqualsValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface PasswordEquals {

	// デフォルトのエラーメッセージ
	String message() default "{com.tie.yennichi.validation.constraints.PasswordEquals.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	// バリデーションアノテーションのリストを定義するためのアノテーション
	public @interface List {
		PasswordEquals[] value();
	}
}