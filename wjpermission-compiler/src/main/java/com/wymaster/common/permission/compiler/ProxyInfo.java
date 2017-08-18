package com.wymaster.common.permission.compiler;

import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.HashMap;
import java.util.Map;

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
    private static final String SUFFIX = "PermissionProxy";


    /*permissions*/
    Map<int[], String[]> syncPermissions = new HashMap<>(1);

    public ProxyInfo(Elements mUtils, TypeElement typeElement) {
        packageName = mUtils.getPackageOf(typeElement).getQualifiedName().toString();
        this.typeElement = typeElement;
        String className = typeElement.getQualifiedName().toString()
                .substring(packageName.length()+1);
        proxyName = className + CONCAT + SUFFIX;
    }
}
