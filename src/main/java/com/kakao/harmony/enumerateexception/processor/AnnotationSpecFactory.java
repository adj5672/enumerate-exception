package com.kakao.harmony.enumerateexception.processor;

import com.squareup.javapoet.AnnotationSpec;
import org.springframework.web.bind.annotation.ExceptionHandler;

class AnnotationSpecFactory {
    private AnnotationSpecFactory() {
    }

    public static AnnotationSpec createExceptionHandler() {
        return AnnotationSpec.builder(ExceptionHandler.class)
            .addMember("value", "CustomRuntimeException.class")
            .build();
    }
}
