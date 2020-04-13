package com.somesh.pemissionmadeeasy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.somesh.permissionmadeeasy.enums.Permission
import com.somesh.permissionmadeeasy.helper.PermissionHelper
import com.somesh.permissionmadeeasy.intefaces.PermissionListener
import java.util.*

class DemoActivity : AppCompatActivity(), View.OnClickListener, PermissionListener {
    private lateinit var rvGranted: RecyclerView
    private lateinit var rvDenied: RecyclerView
    private lateinit var deniedPermissionList: ArrayList<String>
    private lateinit var grantedPermissionList: ArrayList<String>
    private lateinit var permissionHelper: PermissionHelper
    private lateinit var deniedPermissionAdapter: PermissionListAdapter
    private lateinit var grantedPermissionAdapter: PermissionListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        deniedPermissionList = ArrayList()
        grantedPermissionList = ArrayList()
        initView()
        initRecyclerView()
    }

    private fun initView() {
        val btnSingle = findViewById<Button>(R.id.btnSingle)
        val btnMultiple = findViewById<Button>(R.id.btnMultiple)
        val btnOpenSetting = findViewById<Button>(R.id.btnOpenSetting)
        rvGranted = findViewById(R.id.rvGranted)
        rvDenied = findViewById(R.id.rvDenied)
        btnSingle.setOnClickListener(this)
        btnMultiple.setOnClickListener(this)
        btnOpenSetting.setOnClickListener(this)
    }

    private fun initRecyclerView() {
        rvGranted.layoutManager = LinearLayoutManager(this)
        grantedPermissionAdapter = PermissionListAdapter(grantedPermissionList)
        rvGranted.adapter = grantedPermissionAdapter
        rvDenied.layoutManager = LinearLayoutManager(this)
        deniedPermissionAdapter = PermissionListAdapter(deniedPermissionList)
        rvDenied.adapter = deniedPermissionAdapter
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnSingle -> askForSinglePermission()
            R.id.btnMultiple -> askForMultiplePermission()
            R.id.btnOpenSetting -> openPermissionSetting()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, acceptedPermissionList: ArrayList<String>) {
        grantedPermissionList.clear()
        grantedPermissionList.addAll(acceptedPermissionList)
        grantedPermissionAdapter.notifyDataSetChanged()
    }

    override fun onPermissionsDenied(requestCode: Int, deniedPermissionList: ArrayList<String>) {
        this.deniedPermissionList.clear()
        this.deniedPermissionList.addAll(deniedPermissionList)
        deniedPermissionAdapter.notifyDataSetChanged()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun askForSinglePermission() {
        permissionHelper = PermissionHelper.Builder()
                .with(this)
                .requestCode(REQUEST_CODE_SINGLE)
                .setPermissionResultCallback(this)
                .askFor(Permission.CAMERA)
                .build()
        permissionHelper.requestPermissions()
    }

    private fun askForMultiplePermission() {
        permissionHelper = PermissionHelper.Builder()
                .with(this)
                .requestCode(REQUEST_CODE_MULTIPLE)
                .setPermissionResultCallback(this)
                .askFor(Permission.CALENDAR, Permission.CAMERA, Permission.CONTACTS,
                        Permission.LOCATION, Permission.MICROPHONE, Permission.STORAGE,
                        Permission.PHONE, Permission.SMS, Permission.SENSORS)
                .rationalMessage("Permissions are required for app to work properly")
                .build()
        permissionHelper.requestPermissions()
    }

    private fun openPermissionSetting() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    companion object {
        private const val REQUEST_CODE_MULTIPLE = 1011
        private const val REQUEST_CODE_SINGLE = 1022
    }
}