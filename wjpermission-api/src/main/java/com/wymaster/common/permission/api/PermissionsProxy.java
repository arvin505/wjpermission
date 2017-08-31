package com.wymaster.common.permission.api;

/**
 * Created by xiaoyi on 2017-8-31.
 */

public interface PermissionsProxy<T> {
    void rationale(T object, int code);

    void denied(T object, int code);

    void granted(T object, int code);

    boolean customRationale(T object, int code);

    void startSyncRequestPermissionsMethod(T object);
}
