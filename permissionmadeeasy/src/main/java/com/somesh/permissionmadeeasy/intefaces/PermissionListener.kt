package com.somesh.permissionmadeeasy.intefaces

import java.util.*

/**
 * If this code works, it was written by Somesh Kumar  on 28 November, 2017. If not, I don't know who wrote it.
 */
interface PermissionListener {
    fun onPermissionsGranted(requestCode: Int, acceptedPermissionList: ArrayList<String>)
    fun onPermissionsDenied(requestCode: Int, deniedPermissionList: ArrayList<String>)
}