package com.github.danny.enumerateexception.processor;

import org.springframework.http.HttpStatus;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import java.util.List;

class ElementValidation {

    private ElementValidation() {
    }

    public static boolean hasGetCode(List<? extends Element> elements) {
        return elements.stream()
            .filter(element -> element.getKind() == ElementKind.METHOD)
            .map(ExecutableElement.class::cast)
            .anyMatch(element -> element.getSimpleName().contentEquals("getCode")
                && element.getReturnType().toString().equals(int.class.getName()));
    }

    public static boolean hasGetHttpStatus(List<? extends Element> elements) {
        return elements.stream()
            .filter(element -> element.getKind() == ElementKind.METHOD)
            .map(ExecutableElement.class::cast)
            .anyMatch(element -> element.getSimpleName().contentEquals("getHttpStatus")
                && element.getReturnType().toString().equals(HttpStatus.class.getName()));
    }

    public static boolean hasGetMessage(List<? extends Element> elements) {
        return elements.stream()
            .filter(element -> element.getKind() == ElementKind.METHOD)
            .map(ExecutableElement.class::cast)
            .anyMatch(element -> element.getSimpleName().contentEquals("getMessage")
                && element.getReturnType().toString().equals(String.class.getName()));
    }
}
