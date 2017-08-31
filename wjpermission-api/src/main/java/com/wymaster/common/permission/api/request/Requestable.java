package com.wymaster.common.permission.api.request;

/**
 * Created by xiaoyi on 2017-8-31.
 */

public interface Requestable {
    void request(Object object,String permission,int requestCode);
}
