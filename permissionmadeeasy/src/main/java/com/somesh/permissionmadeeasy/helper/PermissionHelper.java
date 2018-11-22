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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.somesh.permissionmadeeasy.helper;


import com.somesh.permissionmadeeasy.enums.Permission;
import com.somesh.permissionmadeeasy.intefaces.PermissionListener;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * If this code works it was written by Somesh Kumar on 23 December, 2017. If not, I don't know who wrote it.
 */
public class PermissionHelper {
    private ArrayList<String> permissionsToAsk;
    private String rationalMessage;
    private PermissionListener permissionListener;
    private AppCompatActivity activity;
    private Fragment fragment;
    private Context context;

    private boolean isFragment;
    private boolean shouldShowRationaleDialog;
    private int requestCode;

    private PermissionHelper() {
        throw new AssertionError("Can't use default constructor. Use PermissionHelper.Builder class to create a new Method of PermissionHelper");
    }

    private PermissionHelper(Builder builder) {
        this.fragment = builder.fragment;
        this.activity = builder.activity;
        this.requestCode = builder.requestCode;
        this.rationalMessage = builder.rationalMessage;
        this.permissionsToAsk = builder.permissionsToAsk;
        this.permissionListener = builder.permissionListener;
        this.context = builder.context;
        this.isFragment = builder.isFragment;
    }

    /**
     * Entry point of {@link PermissionHelper}
     *
     * @return instance of {@link Builder}
     */

    public static IWith Builder() {
        return new Builder();
    }

    /**
     * Method that invokes permission dialog, if permission is already granted or
     * denied (with never asked ticked) then the result is delivered without showing any dialog.
     */
    public void requestPermissions() {
        if (!hasPermissions(context, permissionsToAsk.toArray(new String[0]))) {
            if (shouldShowRationale(permissionsToAsk.toArray(new String[0])) && rationalMessage != null) {
                DialogUtil.getInstance().showAlertDialog(context, null, 0, rationalMessage, "OK", (dialog, which) -> request(), "Cancel", true);
            } else {
                request();
            }
        } else {
            permissionListener.onPermissionsGranted(requestCode, permissionsToAsk);
        }
    }

    /* Shows a dialog for permission */
    private void request() {
        if (isFragment) {
            fragment.requestPermissions(permissionsToAsk.toArray(new String[0]), requestCode);
        } else {
            ActivityCompat.requestPermissions(activity, permissionsToAsk.toArray(new String[0]), requestCode);
        }
    }

    /* Check whether any permission is denied before, if yes then we show a rational dialog for explanation */
    private boolean shouldShowRationale(String... permissions) {
        // Todo : Improve, check if this check can be done with only one call (ActivityCompat) for both fragment and activity
        if (isFragment) {
            for (String permission : permissions) {
                if (fragment.shouldShowRequestPermissionRationale(permission)) {
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

    /* Check if we already have the permission */
    private boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Called by the user when he gets the call in Activity/Fragment
     *
     * @param reqCode      Request Code
     * @param permissions  List of permissions
     * @param grantResults Permission grant result
     */
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


    /**
     * {@link Builder} class for {@link PermissionHelper}.
     * Use only this class to create a new instance of {@link PermissionHelper}
     */
    public static class Builder implements IWith, IRequestCode, IPermissionResultCallback, IAskFor, IBuild {
        ArrayList<String> permissionsToAsk;
        String rationalMessage;
        PermissionListener permissionListener;
        AppCompatActivity activity;
        Fragment fragment;
        Context context;
        int requestCode = -1;
        boolean isFragment;


        @Override
        public IRequestCode with(AppCompatActivity activity) {
            this.activity = activity;
            this.context = activity;
            isFragment = false;
            return this;
        }

        @Override
        public IRequestCode with(Fragment fragment) {
            this.fragment = fragment;
            this.context = fragment.getActivity();
            isFragment = true;
            return this;
        }

        @Override
        public IPermissionResultCallback requestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        @Override
        public IAskFor setPermissionResultCallback(PermissionListener permissionListener) {
            this.permissionListener = permissionListener;
            return this;
        }

        @Override
        public IBuild askFor(Permission... permission) {
            permissionsToAsk = new ArrayList<>();
            for (Permission mPermission : permission) {
                switch (mPermission) {
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                            permissionsToAsk.add(Manifest.permission.BODY_SENSORS);
                        }
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

        @Override
        public PermissionHelper.IBuild rationalMessage(String message) {
            this.rationalMessage = message;
            return this;
        }

        @Override
        public PermissionHelper build() {
            if (this.permissionListener == null) {
                throw new NullPointerException("Permission listener can not be null");
            } else if (this.context == null) {
                throw new NullPointerException("Context can not be null");
            } else if (this.permissionsToAsk.size() == 0) {
                throw new IllegalArgumentException("Not asking for any permission. At least one permission is expected before calling build()");
            } else if (this.requestCode == -1) {
                throw new IllegalArgumentException("Request code is missing");
            } else {
                return new PermissionHelper(this);
            }
        }

    }

    /*Interfaces for builder to make some methods must/required*/

    public interface IWith {
        IRequestCode with(AppCompatActivity activity);

        IRequestCode with(Fragment fragment);
    }

    public interface IRequestCode {
        IPermissionResultCallback requestCode(int requestCode);
    }

    public interface IPermissionResultCallback {
        IAskFor setPermissionResultCallback(PermissionListener permissionListener);
    }

    public interface IAskFor {
        IBuild askFor(Permission... permission);
    }

    public interface IBuild {
        IBuild rationalMessage(String message);

        PermissionHelper build();
    }
}