package com.wymaster.common.permission.api.request;

import android.support.v4.app.Fragment;

import com.wymaster.common.permission.api.PermissionsProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoyi on 2017-8-31.
 */

public class SupportFragmentRequest implements Requestable {
    private static Map<PermissionsProxy, SupportFragmentRequest> map = new HashMap<>();
    private final PermissionsProxy instance;

    private SupportFragmentRequest(PermissionsProxy instance) {
        this.instance = instance;
    }

    public static SupportFragmentRequest getInstance(PermissionsProxy instance) {
        SupportFragmentRequest request = map.get(instance);
        if (request == null) {
            request = new SupportFragmentRequest(instance);
            map.put(instance, request);
        }
        return request;
    }

    @Override
    public void request(Object object, String permission, int requestCode) {
        if (((Fragment) object).shouldShowRequestPermissionRationale(permission)) {
            if (!instance.customRationale(object, requestCode)) {
                instance.rationale(object, requestCode);
                ((Fragment) object).requestPermissions(new String[]{permission}, requestCode);
            }
        } else {
            ((Fragment) object).requestPermissions(new String[]{permission}, requestCode);
        }
    }
}
