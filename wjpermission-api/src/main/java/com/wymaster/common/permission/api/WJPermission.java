package com.wymaster.common.permission.api;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;

import com.wymaster.common.permission.api.request.ActivityRequest;
import com.wymaster.common.permission.api.request.FragmentRequest;
import com.wymaster.common.permission.api.request.SupportFragmentRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoyi on 2017-8-31.
 */

public class WJPermission {
    private static final String PERMISSIONS_PROXY = "$$PermissionsProxy";
    private static Map<String, PermissionsProxy> map = new HashMap<>();
    private static PermissionsProxy instance;

    public static void syncRequestPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(activity);
        syncRequest(activity);
    }

    public static void syncRequestPermissions(android.support.v4.app.Fragment fragment) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(fragment);
        syncRequest(fragment);
    }

    public static void syncRequestPermissions(Fragment fragment) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(fragment);
        syncRequest(fragment);
    }

    private static void syncRequest(Object object) {
        if ((object instanceof Activity) || (object instanceof Fragment) ||
                (object instanceof android.support.v4.app.Fragment)) {
            instance.startSyncRequestPermissionsMethod(object);
        } else {
            throw new IllegalArgumentException(object.getClass().getName() + " is not supported!");
        }
    }

    private static void initProxy(Object object) {
        String name = object.getClass().getName();
        String proxyName = name + PERMISSIONS_PROXY;
        PermissionsProxy proxy = map.get(proxyName);
        try {
            if (proxy == null) {
                proxy = (PermissionsProxy) Class.forName(proxyName).newInstance();
                map.put(proxyName, proxy);
                instance = proxy;
            } else {
                instance = proxy;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestPermission(Activity activity, String permission, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(activity);
        ActivityRequest.getInstance(instance).request(activity, permission, requestCode);
    }

    public static void requestPermission(Fragment fragment, String permission, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(fragment);
        FragmentRequest.getInstance(instance).request(fragment, permission, requestCode);
    }

    public static void requestPermission(android.support.v4.app.Fragment fragment, String permission, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(fragment);
        SupportFragmentRequest.getInstance(instance).request(fragment, permission, requestCode);
    }

    public static void onRequestPermissionsResult(Object object, int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            instance.granted(object, requestCode);
        } else {
            instance.denied(object, requestCode);
        }
    }
}
