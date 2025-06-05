package com.example.movies.API.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.example.movies.API.validator.PaginationValidator;

@Constraint(validatedBy = PaginationValidator.class)
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPagination {
    String message() default "Invalid pagination parameters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
