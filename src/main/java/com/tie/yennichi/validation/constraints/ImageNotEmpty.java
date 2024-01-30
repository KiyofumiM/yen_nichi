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
 * 画像が空かどうかを検証するためのカスタムバリデーションアノテーション
 */
@Documented
@Constraint(validatedBy = ImageNotEmptyValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface ImageNotEmpty {

    String message() default "{com.tie.yennichi.validation.constraints.ImageNotEmpty.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}