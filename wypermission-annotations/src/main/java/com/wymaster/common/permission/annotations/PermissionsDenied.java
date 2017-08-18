package com.wymaster.common.permission.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xiaoyi on 2017/8/18.
 * 权限被拒绝时的回调
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface PermissionsDenied {
    int[] value();
}
