package com.goormthon.samsamejo.annotation;

import com.goormthon.samsamejo.validator.FileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface ValidFile {

    String message() default "INVALID_FILE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
