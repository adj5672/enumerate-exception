package com.kakao.harmony.enumerateexception.processor;

import com.google.auto.service.AutoService;
import com.kakao.harmony.enumerateexception.annotation.EnumeratedException;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.kakao.harmony.controlleradvice.annotation.EnumeratedException")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class EnumeratedExceptionProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(EnumeratedException.class);

        for (Element element : elements) {
            if (element.getKind() != ElementKind.ENUM) {
                printError("@EnumeratedException supports only enum class");
            } else {
                TypeElement typeElement = (TypeElement) element;
                createExceptionClasses(typeElement);
                createRestControllerAdviceClass(typeElement);
            }
        }

        return true;
    }

    private void createExceptionClasses(TypeElement enumElement) {
        List<? extends Element> enumEnclosedElements = enumElement.getEnclosedElements();

        // Checking class has getCode(), getHttpStatus(), getMessage()
        checkEssentialMethodsExist(enumEnclosedElements);

        // for enumConstant element : TypeSpec 생성
        List<TypeSpec> typeSpecStream = enumEnclosedElements.stream()
            .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.ENUM_CONSTANT)
            .map(VariableElement.class::cast)
            .map(TypeSpecFactory::createExceptionTypeSpec)
            .collect(Collectors.toList());

        // JavaFile class 생성
        try {
            for (TypeSpec typeSpec : typeSpecStream) {
                JavaFile.builder(ClassName.get(enumElement).packageName(), typeSpec)
                    .build()
                    .writeTo(processingEnv.getFiler());
            }
        } catch (Exception e) {
            printError("Failed to write Exception JavaFile - " + e.getMessage());
        }
    }

    private void createRestControllerAdviceClass(TypeElement enumElement) {
        TypeSpec typeSpec = TypeSpecFactory.createRestControllerAdviceTypeSpec();

        try {
            JavaFile.builder(ClassName.get(enumElement).packageName(), typeSpec)
                .build()
                .writeTo(processingEnv.getFiler());
        } catch (Exception e) {
            printError("Failed to write Exception JavaFile - " + e.getMessage());
        }
    }

    private void checkEssentialMethodsExist(List<? extends Element> elements) {
        if (!ElementValidation.hasGetCode(elements)) {
            printError("Enum class with @EnumeratedException must have getCode() method that returns int");
        }
        if (!ElementValidation.hasGetHttpStatus(elements)) {
            printError("Enum class with @EnumeratedException must have getHttpStatus() method that returns HttpStatus");
        }
        if (!ElementValidation.hasGetMessage(elements)) {
            printError("Enum class with @EnumeratedException must have getMessage() method that returns String");
        }
    }

    private void printMessage(String message) {
        processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.NOTE, message);
    }

    private void printError(String message) {
        processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR, message);
    }
}
