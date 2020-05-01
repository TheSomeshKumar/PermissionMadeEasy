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
package com.somesh.permissionmadeeasy.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.somesh.permissionmadeeasy.enums.Permission
import com.somesh.permissionmadeeasy.intefaces.PermissionListener
import java.util.*

/**
 * If this code works it was written by Somesh Kumar on 23 December, 2017. If not, I don't know who wrote it.
 */
class PermissionHelper {
    private lateinit var permissionsToAsk: ArrayList<String>
    private var rationalMessage: String? = null
    private lateinit var permissionListener: PermissionListener
    private var activity: Activity? = null
    private var fragment: Fragment? = null
    private lateinit var context: Context
    private var isFragment = false
    private var shouldShowRationaleDialog = false
    private var requestCode = 0

    private constructor() {
        throw AssertionError("Can't use default constructor. Use PermissionHelper.Builder class to create a new Method of PermissionHelper")
    }

    private constructor(builder: Builder) {
        fragment = builder.fragment
        activity = builder.activity
        requestCode = builder.requestCode
        rationalMessage = builder.rationalMessage
        permissionsToAsk = builder.permissionsToAsk
        permissionListener = builder.permissionListener
        context = builder.context
        isFragment = builder.isFragment
    }

    companion object {
        /**
         * Entry point of [PermissionHelper]
         *
         * @return instance of [Builder]
         */

        @JvmStatic
        fun Builder(): IWith = PermissionHelper.Builder()
    }


    /**
     * Method that invokes permission dialog, if permission is already granted or
     * denied (with never asked ticked) then the result is delivered without showing any dialog.
     */
    fun requestPermissions() {
        if (!hasPermissions(context, *permissionsToAsk.toTypedArray())) {
            if (shouldShowRationale(*permissionsToAsk.toTypedArray()) && rationalMessage != null) {
                DialogUtil.showAlertDialog(context, null, 0, rationalMessage!!, "OK", DialogInterface.OnClickListener { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                    request()
                }, "Cancel", true)
            } else {
                request()
            }
        } else {
            permissionListener.onPermissionsGranted(requestCode, permissionsToAsk)
        }
    }

    /* Shows a dialog for permission */
    private fun request() {
        when {
            isFragment -> fragment?.requestPermissions(permissionsToAsk.toTypedArray(), requestCode)
            else -> activity?.let { ActivityCompat.requestPermissions(it, permissionsToAsk.toTypedArray(), requestCode) }
        }
    }

    /* Check whether any permission is denied before, if yes then we show a rational dialog for explanation */
    private fun shouldShowRationale(vararg permissions: String): Boolean {
        fragment?.let {
            for (permission in permissions) {
                if (it.shouldShowRequestPermissionRationale(permission)) {
                    shouldShowRationaleDialog = true
                }
            }
        } ?: activity?.let {
            for (permission in permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(it, permission)) {
                    shouldShowRationaleDialog = true
                }
            }
        }

        return shouldShowRationaleDialog
    }

    /* Check if we already have the permission */
    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * Called by the user when he gets the call in Activity/Fragment
     *
     * @param reqCode      Request Code
     * @param permissions  List of permissions
     * @param grantResults Permission grant result
     */
    fun onRequestPermissionsResult(reqCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == reqCode) {
            val grantedPermissionList = ArrayList<String>()
            val deniedPermissionList = ArrayList<String>()
            for (i in grantResults.indices) {
                val grantResult = grantResults[i]
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissionList.add(permissions[i])
                } else {
                    deniedPermissionList.add(permissions[i])
                }
            }
            if (grantedPermissionList.isNotEmpty()) {
                permissionListener.onPermissionsGranted(requestCode, grantedPermissionList)
            }
            if (deniedPermissionList.isNotEmpty()) {
                permissionListener.onPermissionsDenied(requestCode, deniedPermissionList)
            }
        }
    }

    /**
     * [Builder] class for [PermissionHelper].
     * Use only this class to create a new instance of [PermissionHelper]
     */
    private class Builder : IWith, IRequestCode, IPermissionResultCallback, IAskFor, IBuild {
        lateinit var permissionsToAsk: ArrayList<String>
        var rationalMessage: String? = null
        lateinit var permissionListener: PermissionListener
        var activity: Activity? = null
        var fragment: Fragment? = null
        lateinit var context: Context
        var requestCode = -1
        var isFragment = false

        override fun with(@NonNull activity: Activity): IRequestCode {
            this.activity = activity
            this.context = activity
            this.isFragment = false
            return this
        }

        override fun with(@NonNull fragment: Fragment): IRequestCode {
            this.fragment = fragment
            this.context = fragment.requireContext()
            this.isFragment = true
            return this
        }

        override fun requestCode(@NonNull requestCode: Int): IPermissionResultCallback {
            this.requestCode = requestCode
            return this
        }

        override fun setPermissionResultCallback(@NonNull permissionListener: PermissionListener): IAskFor {
            this.permissionListener = permissionListener
            return this
        }

        override fun askFor(vararg permission: Permission): IBuild {
            this.permissionsToAsk = ArrayList()
            for (mPermission in permission) {
                when (mPermission) {
                    Permission.CALENDAR -> permissionsToAsk.add(Manifest.permission.WRITE_CALENDAR)
                    Permission.CAMERA -> permissionsToAsk.add(Manifest.permission.CAMERA)
                    Permission.CONTACTS -> permissionsToAsk.add(Manifest.permission.READ_CONTACTS)
                    Permission.LOCATION -> permissionsToAsk.add(Manifest.permission.ACCESS_FINE_LOCATION)
                    Permission.MICROPHONE -> permissionsToAsk.add(Manifest.permission.RECORD_AUDIO)
                    Permission.PHONE -> permissionsToAsk.add(Manifest.permission.CALL_PHONE)
                    Permission.SENSORS -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                        permissionsToAsk.add(Manifest.permission.BODY_SENSORS)
                    }
                    Permission.SMS -> permissionsToAsk.add(Manifest.permission.SEND_SMS)
                    Permission.STORAGE -> permissionsToAsk.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
            return this
        }

        override fun rationalMessage(message: String): IBuild {
            this.rationalMessage = message
            return this
        }

        override fun build(): PermissionHelper = when {
            permissionsToAsk.size == 0 -> throw IllegalArgumentException("Not asking for any permission. At least one permission is expected before calling build()")
            requestCode == -1 -> throw IllegalArgumentException("Request code is missing")
            else -> PermissionHelper(this)
        }
    }

    /*Interfaces for builder to make some methods must/required*/
    interface IWith {
        fun with(activity: Activity): IRequestCode
        fun with(fragment: Fragment): IRequestCode
    }

    interface IRequestCode {
        fun requestCode(requestCode: Int): IPermissionResultCallback
    }

    interface IPermissionResultCallback {
        fun setPermissionResultCallback(permissionListener: PermissionListener): IAskFor
    }

    interface IAskFor {
        fun askFor(vararg permission: Permission): IBuild
    }

    interface IBuild {
        fun rationalMessage(message: String): IBuild
        fun build(): PermissionHelper
    }


}