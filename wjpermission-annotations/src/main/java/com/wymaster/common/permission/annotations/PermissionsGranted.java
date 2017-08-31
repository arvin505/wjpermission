package com.wymaster.common.permission.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.xml.bind.ValidationEvent;

/**
 * Created by xiaoyi on 2017/8/18.
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface PermissionsGranted {
    int[] value();
}
