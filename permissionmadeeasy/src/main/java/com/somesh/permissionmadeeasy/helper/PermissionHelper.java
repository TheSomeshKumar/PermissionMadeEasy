/*
 * MIT License
 *
 * Copyright (c) 2017 Somesh Kumar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.somesh.permissionmadeeasy.helper;


import com.somesh.permissionmadeeasy.enums.PermissionEnum;
import com.somesh.permissionmadeeasy.intefaces.PermissionListener;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * If this code works it was written by Somesh Kumar on 23 December, 2017. If not, I don't know who wrote it.
 */
public class PermissionHelper {
    private ArrayList<String> permissionsToAsk;
    private String rationalMessage;
    private PermissionListener permissionListener;
    private Activity activity;
    private Fragment fragment;
    private Context context;
    private boolean isFragment;
    private boolean shouldShowRationaleDialog;
    private int requestCode;


    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    public PermissionHelper askFor(PermissionEnum... permissionEnum) {
        permissionsToAsk = new ArrayList<>();
        for (PermissionEnum mPermissionEnum : permissionEnum) {
            switch (mPermissionEnum) {
                case CALENDAR:
                    permissionsToAsk.add(Manifest.permission.WRITE_CALENDAR);
                    break;
                case CAMERA:
                    permissionsToAsk.add(Manifest.permission.CAMERA);
                    break;
                case CONTACTS:
                    permissionsToAsk.add(Manifest.permission.READ_CONTACTS);
                    break;
                case LOCATION:
                    permissionsToAsk.add(Manifest.permission.ACCESS_FINE_LOCATION);
                    break;
                case MICROPHONE:
                    permissionsToAsk.add(Manifest.permission.RECORD_AUDIO);
                    break;
                case PHONE:
                    permissionsToAsk.add(Manifest.permission.CALL_PHONE);
                    break;
                case SENSORS:
                    permissionsToAsk.add(Manifest.permission.BODY_SENSORS);
                    break;
                case SMS:
                    permissionsToAsk.add(Manifest.permission.SEND_SMS);
                    break;
                case STORAGE:
                    permissionsToAsk.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    break;
            }
        }
        return this;
    }

    public PermissionHelper with(Activity activity) {
        this.activity = activity;
        this.context = activity;
        isFragment = false;
        return this;
    }

    public PermissionHelper with(Fragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getActivity();
        isFragment = true;
        return this;
    }


    public PermissionHelper rationalMessage(String rationalMessage) {
        this.rationalMessage = rationalMessage;
        return this;
    }

    public PermissionHelper requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public PermissionHelper build() {
        requestPermissions();
        return this;
    }

    private void requestPermissions() {
        if (!hasPermissions(context, permissionsToAsk.toArray(new String[permissionsToAsk.size()]))) {
            if (shouldShowRationale(permissionsToAsk.toArray(new String[permissionsToAsk.size()]))) {
                DialogUtil.getInstance().showAlertDialog(context, null, 0, rationalMessage, "OK", (dialog, which) -> requestPermission(), true);
            } else {
                requestPermission();
            }
        } else {
            permissionListener.onPermissionsGranted(requestCode, permissionsToAsk);
        }
    }

    private void requestPermission() {
        if (isFragment) {
            FragmentCompat.requestPermissions(fragment, permissionsToAsk.toArray(new String[permissionsToAsk.size()]), requestCode);
        } else {
            ActivityCompat.requestPermissions(activity, permissionsToAsk.toArray(new String[permissionsToAsk.size()]), requestCode);
        }
    }

    private boolean shouldShowRationale(String... permissions) {
        if (isFragment) {
            for (String permission : permissions) {
                if (FragmentCompat.shouldShowRequestPermissionRationale(fragment, permission)) {
                    shouldShowRationaleDialog = true;
                }
            }
        } else {
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    shouldShowRationaleDialog = true;
                }
            }
        }


        return shouldShowRationaleDialog;


    }


    public PermissionHelper setPermissionResultCallback(PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
        return this;
    }

    public boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int reqCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == reqCode) {
            ArrayList<String> grantedPermissionList = new ArrayList<>();
            ArrayList<String> deniedPermissionList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissionList.add(permissions[i]);
                } else {
                    deniedPermissionList.add(permissions[i]);
                }
            }

            if (!grantedPermissionList.isEmpty()) {
                permissionListener.onPermissionsGranted(requestCode, grantedPermissionList);
            }
            if (!deniedPermissionList.isEmpty()) {
                permissionListener.onPermissionsDenied(requestCode, deniedPermissionList);
            }
        }
    }


}
