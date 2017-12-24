package com.somesh.permissionmadeeasy.intefaces;

import java.util.ArrayList;

/**
 * If this code works, it was written by Somesh Kumar  on 28 November, 2017. If not, I don't know who wrote it.
 */
public interface PermissionListener {
    void onPermissionsGranted(int requestCode, ArrayList<String> acceptedPermissionList);

    void onPermissionsDenied(int requestCode, ArrayList<String> deniedPermissionList);
}
