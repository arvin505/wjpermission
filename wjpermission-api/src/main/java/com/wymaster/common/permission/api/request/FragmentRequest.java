package com.wymaster.common.permission.api.request;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;

import com.wymaster.common.permission.api.PermissionsProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoyi on 2017-8-31.
 */

public class FragmentRequest implements Requestable {
    private final PermissionsProxy instance;
    private static Map<PermissionsProxy, FragmentRequest> map = new HashMap<>();

    private FragmentRequest(PermissionsProxy instance) {
        this.instance = instance;
    }

    public static FragmentRequest getInstance(PermissionsProxy instance) {
        FragmentRequest request = map.get(instance);
        if (request == null) {
            request = new FragmentRequest(instance);
            map.put(instance, request);
        }
        return request;
    }

    @TargetApi(Build.VERSION_CODES.M)
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
