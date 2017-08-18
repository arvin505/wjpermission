package com.wymaster.common.permission.compiler;

import com.google.auto.service.AutoService;
import com.wymaster.common.permission.annotations.PermissionsCustomRationale;
import com.wymaster.common.permission.annotations.PermissionsDenied;
import com.wymaster.common.permission.annotations.PermissionsGranted;
import com.wymaster.common.permission.annotations.PermissionsRationale;
import com.wymaster.common.permission.annotations.PermissionsRequestSync;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by arvin on 2017/8/18.
 */

@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {
    private Filer mFiler;
    private Elements mUtils;
    private Map<String, ProxyInfo> map = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>(5);
        set.add(PermissionsGranted.class.getCanonicalName());
        set.add(PermissionsRationale.class.getCanonicalName());
        set.add(PermissionsCustomRationale.class.getCanonicalName());
        set.add(PermissionsDenied.class.getCanonicalName());
        set.add(PermissionsRequestSync.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        map.clear();
        if (!isAnnotatedWithClass(roundEnvironment, PermissionsRequestSync.class)) return false;
        if (!isAnnotatedWithMethod(roundEnvironment, PermissionsGranted.class)) return false;
        if (!isAnnotatedWithMethod(roundEnvironment, PermissionsDenied.class)) return false;
        if (!isAnnotatedWithMethod(roundEnvironment, PermissionsRationale.class)) return false;
        if (!isAnnotatedWithMethod(roundEnvironment, PermissionsCustomRationale.class)) return false;
        return true;
    }

    private boolean isAnnotatedWithClass(RoundEnvironment roundEnv, Class<? extends Annotation> clazz) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(clazz);
        for (Element element : elements) {
            if (isValid(element)) return false;
            TypeElement typeElement = (TypeElement) element;
            String typeName = typeElement.getQualifiedName().toString();
            ProxyInfo info = map.get(typeName);
            if (info == null) {
                info = new ProxyInfo(mUtils, typeElement);
                map.put(typeName, info);
            }

            Annotation anno = typeElement.getAnnotation(clazz);
            if (anno instanceof PermissionsRequestSync) {
                String[] permissions = ((PermissionsRequestSync) anno).permission();
                int[] value = ((PermissionsRequestSync) anno).value();
                if (permissions.length != value.length) {
                    error(typeElement, "permissions's length not equals value's length");
                    return false;
                }
                info.syncPermissions.put(value, permissions);
            } else {
                error(element, "%s not support.", element);
            }
        }

        return true;
    }

    private boolean isAnnotatedWithMethod(RoundEnvironment roundEnv, Class<? extends Annotation> clazz) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(clazz);
        for (Element element : elements) {
            if (isValid(element)) return false;
            ExecutableElement method = (ExecutableElement) element;
            TypeElement typeElement = (TypeElement) method.getEnclosingElement();
            String typeName = typeElement.getQualifiedName().toString();
            String methodName = method.getSimpleName().toString();
            ProxyInfo info = map.get(typeName);
            if (info == null) {
                info = new ProxyInfo(mUtils, typeElement);
                map.put(typeName, info);
            }
            Annotation anno = method.getAnnotation(clazz);
            if (anno instanceof PermissionsGranted) {

            } else if (anno instanceof PermissionsDenied) {

            } else if (anno instanceof PermissionsRationale) {

            } else if (anno instanceof PermissionsCustomRationale) {

            }
        }
        return true;
    }

    private boolean isValid(Element element) {
        return element.getModifiers().contains(Modifier.PRIVATE)
                || element.getModifiers().contains(Modifier.ABSTRACT);
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }

}
