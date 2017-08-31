package com.wymaster.common.permission.compiler;

import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.HashMap;
import java.util.Map;
import java.util.UnknownFormatConversionException;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by arvin on 2017/8/18.
 */

public class ProxyInfo {
    private final String packageName;
    private final String proxyName;
    private final TypeElement typeElement;
    private static final String CONCAT = "$$";
    private static final String SUFFIX = "PermissionsProxy";

    //methodname => requestCodes
    Map<String, int[]> grantedMap = new HashMap<>();
    Map<String, int[]> deniedMap = new HashMap<>();
    Map<String, int[]> rationaleMap = new HashMap<>();
    Map<String, int[]> customRationaleMap = new HashMap<>();

    //requestCodes => methodName
    Map<Integer, String> singleGrantmap = new HashMap<>();
    Map<Integer, String> singleDeniedMap = new HashMap<>();
    Map<Integer, String> singleRationaleMap = new HashMap<>();
    Map<Integer, String> singleCustomRationaleMap = new HashMap<>();

    /*permissions*/
    Map<int[], String[]> syncPermissions = new HashMap<>(1);
    int firstRequestCode;
    String firstRequestPermission;

    public ProxyInfo(Elements mUtils, TypeElement typeElement) {
        packageName = mUtils.getPackageOf(typeElement).getQualifiedName().toString();
        this.typeElement = typeElement;
        String className = typeElement.getQualifiedName().toString()
                .substring(packageName.length() + 1);
        proxyName = className + CONCAT + SUFFIX;
    }

    public String getProxyName() {
        return proxyName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(packageName).append(";\n\n")
                .append("import com.wymaster.common.permission.api.*;\n")
                .append("public class ").append(proxyName).append(" implements ")
                .append(SUFFIX).append("<").append(typeElement.getSimpleName())
                .append(">{\n\n");
        generateMethodCode(builder);

        builder.append("}\n");
        return builder.toString();
    }

    private void generateMethodCode(StringBuilder builder) {
        generateGrantedMethod(builder);
        generateDeniedMethod(builder);
        generateRationaleMethod(builder);
        generateCustomRationaleMethod(builder);
        generateSyncRequestPermissionsMethod(builder);
    }

    private void generateSyncRequestPermissionsMethod(StringBuilder builder) {
        builder.append("@Override\n")
                .append("public void startSyncRequestPermissionsMethod(")
                .append(typeElement.getSimpleName()).append(" object){\n")
                .append("WJPermission.requestPermission(object,\"")
                .append(firstRequestPermission)
                .append("\",").append(firstRequestCode).append(");\n")
                .append("}\n\n");

    }

    private void generateCustomRationaleMethod(StringBuilder builder) {
        builder.append("@Override\n")
                .append("public boolean customRationale(")
                .append(typeElement.getSimpleName())
                .append(" object, int code) {\n")
                .append("switch (code) {\n");
        for (String methodName : customRationaleMap.keySet()) {
            int[] ints = customRationaleMap.get(methodName);
            for (int requestCode : ints) {
                builder.append("case ").append(requestCode).append(":\n");
                if (singleCustomRationaleMap.containsKey(requestCode)) {
                    singleCustomRationaleMap.remove(requestCode);
                }
                builder.append("object.").append(methodName).append("(code);\nreturn true;");
            }
        }
        for (Integer requestCode : singleCustomRationaleMap.keySet()) {
            builder.append("case ").append(requestCode).append(" :\n")
                    .append("object.").append(singleCustomRationaleMap.get(requestCode)).append("();")
                    .append("\nreturn true;\n");
        }
        builder.append("default:\nreturn false;\n}\n}\n");
    }

    private void generateRationaleMethod(StringBuilder builder) {
        builder.append("@Override\n")
                .append("public void rationale(")
                .append(typeElement.getSimpleName())
                .append(" object, int code){\n")
                .append("switch(code) {\n");

        for (String methodName : rationaleMap.keySet()) {
            int[] ints = rationaleMap.get(methodName);
            for (int requestCode : ints) {
                builder.append("case ").append(requestCode).append(":\n{");
                builder.append("object.").append(methodName).append("(").append(requestCode).append(");\n");
                builder.append("break;}\n");
                if (singleRationaleMap.containsKey(requestCode)) {
                    singleRationaleMap.remove(requestCode);
                }
            }
        }

        for (Integer requestCode : singleRationaleMap.keySet()) {
            builder.append("case ").append(requestCode).append(": {\n")
                    .append("object.").append(singleRationaleMap.get(requestCode)).append("();\nbreak;\n}");
        }

        builder.append("default:\nbreak;\n").append("\n}\n}\n");
    }

    private void generateGrantedMethod(StringBuilder builder) {
        builder.append("@Override\n")
                .append("public void granted(")
                .append(typeElement.getSimpleName())
                .append(" object, int code){\n")
                .append("switch(code) {\n");
        for (String methodName : grantedMap.keySet()) {
            int[] ints = grantedMap.get(methodName);
            for (int requestCode : ints) {
                builder.append("case ").append(requestCode).append(":\n")
                        .append("object.").append(methodName).append("(")
                        .append(requestCode).append(");\n");
                addSyncRequestPermissionMethod(builder, requestCode);
                builder.append("break;\n");
                if (singleGrantmap.containsKey(requestCode)) {
                    singleGrantmap.remove(requestCode);
                }
            }
        }
        for (Integer requestCode : singleGrantmap.keySet()) {
            builder.append("case ").append(requestCode).append(":\n")
                    .append("object.").append(singleGrantmap.get(requestCode)).append("();\nbreak;\n");
        }
        builder.append("default:\nbreak;\n}\n}\n");
    }

    private void generateDeniedMethod(StringBuilder builder) {
        builder.append("@Override\n")
                .append("public void denied(")
                .append(typeElement.getSimpleName())
                .append(" object, int code){\n")
                .append("switch(code) {\n");
        for (String methodName : deniedMap.keySet()) {
            int[] ints = deniedMap.get(methodName);
            for (int requestCode : ints) {
                builder.append("case ").append(requestCode).append(":\n")
                        .append("object.").append(methodName).append("(")
                        .append(requestCode).append(");\n");
                addSyncRequestPermissionMethod(builder, requestCode);
                builder.append("break;\n");
                if (singleDeniedMap.containsKey(requestCode)) {
                    singleDeniedMap.remove(requestCode);
                }
            }
        }

        for (Integer requestCode : singleDeniedMap.keySet()) {
            builder.append("case ").append(requestCode).append(" :\n")
                    .append("object.").append(singleDeniedMap.get(requestCode)).append("();\nbreak;\n");
        }

        builder.append("default:\nbreak;\n").append("\n}\n}\n");
    }

    private void addSyncRequestPermissionMethod(StringBuilder builder, int requestCode) {
        for (int[] requestCodes : syncPermissions.keySet()) {
            int length = requestCodes.length;
            String[] permissions = syncPermissions.get(requestCodes);
            firstRequestCode = requestCodes[0];
            firstRequestPermission = permissions[0];
            for (int i = 0; i < length - 1; i++) {
                if (requestCodes[i] == requestCode) {
                    builder.append("WJPermission.requestPermission(object,\"")
                            .append(permissions[i + 1]).append("\",").append(requestCodes[i + 1])
                            .append(");\n");
                }
            }
        }
    }
}
