package com.tie.yennichi.validation.constraints;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * PasswordEqualsアノテーションで指定されたフィールド同士が等しいかどうかを検証するためのバリデータ（PasswordEqualsValidator）を実装するためのクラス
 */
//PasswordEqualsアノテーションの実装
public class PasswordEqualsValidator implements ConstraintValidator<PasswordEquals, Object> {

	private String field1;
	private String field2;
	private String message;

	// 初期化メソッド（アノテーションのパラメーターの初期化）
	@Override
	public void initialize(PasswordEquals annotation) {
		field1 = "password";
		field2 = "passwordConfirmation";
		message = annotation.message();
	}

	// 実際の検証処理
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		
		// BeanWrapperを使用してオブジェクトのプロパティにアクセス
		BeanWrapper beanWrapper = new BeanWrapperImpl(value);
		String field1Value = (String) beanWrapper.getPropertyValue(field1);
		String field2Value = (String) beanWrapper.getPropertyValue(field2);

		// フィールド1とフィールド2がどちらも空白または等しい場合は検証成功
		if ((field1Value.isEmpty() || field2Value.isEmpty()) || Objects.equals(field1Value, field2Value)) {
			return true;
		} else {

			// フィールド1とフィールド2が異なる場合、エラーメッセージを設定して検証失敗
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message).addPropertyNode(field2).addConstraintViolation();
			return false;
		}
	}

}