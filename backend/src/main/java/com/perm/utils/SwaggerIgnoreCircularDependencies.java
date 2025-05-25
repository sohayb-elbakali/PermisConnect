package com.perm.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Hidden;

/**
 * Custom annotation to mark methods or fields that should be ignored by Swagger
 * This can help prevent circular dependency issues in the API documentation
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Hidden
public @interface SwaggerIgnoreCircularDependencies {
    // This is a marker annotation - no additional methods needed
}
