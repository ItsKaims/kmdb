package com.example.movies.API.validator;

import com.example.movies.API.annotation.ValidPagination;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PaginationValidator implements ConstraintValidator<ValidPagination, Pageable> {

    private static final int MIN_PAGE = 0;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @Override
    public void initialize(ValidPagination constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext context) {
        if (pageable == null) {
            return true; // null is valid, will use default values
        }

        if (pageable instanceof PageRequest) {
            PageRequest pageRequest = (PageRequest) pageable;
            
            if (pageRequest.getPageNumber() < MIN_PAGE) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Page number must be non-negative")
                        .addConstraintViolation();
                return false;
            }

            if (pageRequest.getPageSize() > MAX_PAGE_SIZE) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Page size must not exceed " + MAX_PAGE_SIZE)
                        .addConstraintViolation();
                return false;
            }

            if (pageRequest.getPageSize() < 1) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Page size must be at least 1")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
