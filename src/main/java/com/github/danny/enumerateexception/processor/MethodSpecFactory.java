package com.github.danny.enumerateexception.processor;

import com.github.danny.enumerateexception.exception.CustomRuntimeException;
import com.github.danny.enumerateexception.response.BaseErrorResponse;
import com.squareup.javapoet.MethodSpec;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.List;

import static com.github.danny.enumerateexception.processor.AnnotationSpecFactory.createExceptionHandler;

class MethodSpecFactory {

    private MethodSpecFactory() {
    }

    public static List<MethodSpec> createExceptionConstructors() {
        MethodSpec noArgs = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement("super()")
            .build();

        MethodSpec messageArgs = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(String.class, "message")
            .addStatement("super(message)")
            .build();

        MethodSpec causeArgs = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Throwable.class, "cause")
            .addStatement("super(cause)")
            .build();

        MethodSpec allArgs = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(String.class, "message")
            .addParameter(Throwable.class, "cause")
            .addStatement("super(message, cause)")
            .build();

        return List.of(noArgs, messageArgs, causeArgs, allArgs);
    }

    public static List<MethodSpec> createGetters(VariableElement enumConstantElement) {
        String type = enumConstantElement.asType().toString();
        String enumConstant = enumConstantElement.getSimpleName().toString();

        MethodSpec getCode = MethodSpec.methodBuilder("getCode")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addStatement("return $1L.$2L.getCode()", type, enumConstant)
            .returns(int.class)
            .build();

        MethodSpec getHttpStatus = MethodSpec.methodBuilder("getHttpStatus")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addStatement("return $1L.$2L.getHttpStatus()", type, enumConstant)
            .returns(HttpStatus.class)
            .build();

        MethodSpec getMessage = MethodSpec.methodBuilder("getMessage")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addStatement("return super.getMessage() == null ? $1L.$2L.getMessage() : super.getMessage()", type, enumConstant)
            .returns(String.class)
            .build();

        return List.of(getCode, getHttpStatus, getMessage);
    }

    public static MethodSpec createRestControllerAdvice() {
        return MethodSpec.methodBuilder("handle")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(createExceptionHandler())
            .addParameter(CustomRuntimeException.class, "e")
            .addStatement("$1L response = new $1L(e.getCode(), e.getMessage())", BaseErrorResponse.class.getName())
            .addStatement("return ResponseEntity.status(e.getHttpStatus()).body(response)")
            .returns(ResponseEntity.class)
            .build();
    }
}