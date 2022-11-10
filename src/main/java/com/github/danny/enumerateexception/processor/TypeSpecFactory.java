package com.github.danny.enumerateexception.processor;

import com.github.danny.enumerateexception.exception.CustomRuntimeException;
import com.squareup.javapoet.TypeSpec;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.stream.Collectors;

class TypeSpecFactory {

    private TypeSpecFactory() {
    }

    public static TypeSpec createExceptionTypeSpec(VariableElement element) {
        String enumConstant = element.getSimpleName().toString();

        return TypeSpec.classBuilder(snakeToCamel(enumConstant) + "Exception")
            .addModifiers(Modifier.PUBLIC)
            .superclass(CustomRuntimeException.class)
            .addMethods(MethodSpecFactory.createExceptionConstructors())
            .addMethods(MethodSpecFactory.createGetters(element))
            .build();
    }

    public static TypeSpec createRestControllerAdviceTypeSpec() {
        return TypeSpec.classBuilder("CustomRuntimeExceptionAdvice")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Order.class)
            .addAnnotation(RestControllerAdvice.class)
            .addMethod(MethodSpecFactory.createRestControllerAdvice())
            .build();
    }

    private static String snakeToCamel(String text) {
        return Arrays.stream(text.split("_"))
            .map(String::toLowerCase)
            .map(org.springframework.util.StringUtils::capitalize)
            .collect(Collectors.joining(""));
    }
}
