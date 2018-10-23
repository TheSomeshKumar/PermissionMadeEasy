package com.somesh.pemissionmadeeasy;

import com.somesh.permissionmadeeasy.enums.PermissionEnum;
import com.somesh.permissionmadeeasy.helper.PermissionHelper;
import com.somesh.permissionmadeeasy.intefaces.PermissionListener;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener, PermissionListener {

    private static final int REQUEST_CODE_MULTIPLE = 1011;
    private static final int REQUEST_CODE_SINGLE = 1022;
    private RecyclerView rvGranted;
    private RecyclerView rvDenied;

    private ArrayList<String> mDeniedPermissionList;
    private ArrayList<String> mGrantedPermissionList;
    private PermissionHelper permissionHelper;
    private PermissionListAdapter deniedPermissionAdapter;
    private PermissionListAdapter grantedPermissionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mDeniedPermissionList = new ArrayList<>();
        mGrantedPermissionList = new ArrayList<>();
        permissionHelper = new PermissionHelper();

        initView();
        initRecyclerView();
    }


    private void initView() {
        Button btnSingle = findViewById(R.id.btnSingle);
        Button btnMultiple = findViewById(R.id.btnMultiple);
        Button btnOpenSetting = findViewById(R.id.btnOpenSetting);
        rvGranted = findViewById(R.id.rvGranted);
        rvDenied = findViewById(R.id.rvDenied);

        btnSingle.setOnClickListener(this);
        btnMultiple.setOnClickListener(this);
        btnOpenSetting.setOnClickListener(this);
    }

    private void initRecyclerView() {
        rvGranted.setLayoutManager(new LinearLayoutManager(this));
        grantedPermissionAdapter = new PermissionListAdapter(mGrantedPermissionList);
        rvGranted.setAdapter(grantedPermissionAdapter);

        rvDenied.setLayoutManager(new LinearLayoutManager(this));
        deniedPermissionAdapter = new PermissionListAdapter(mDeniedPermissionList);
        rvDenied.setAdapter(deniedPermissionAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSingle:
                askForSinglePermission();
                break;

            case R.id.btnMultiple:
                askForMultiplePermission();
                break;

            case R.id.btnOpenSetting:
                openPermissionSetting();
                break;
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, ArrayList<String> acceptedPermissionList) {
        mGrantedPermissionList.clear();
        mGrantedPermissionList.addAll(acceptedPermissionList);
        grantedPermissionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPermissionsDenied(int requestCode, ArrayList<String> deniedPermissionList) {
        mDeniedPermissionList.clear();
        mDeniedPermissionList.addAll(deniedPermissionList);
        deniedPermissionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void askForSinglePermission() {
        permissionHelper.with(this)
                .askFor(PermissionEnum.LOCATION)
                .rationalMessage("Permissions are required for app to work properly")
                .requestCode(REQUEST_CODE_SINGLE)
                .setPermissionResultCallback(this)
                .build();
    }


    private void askForMultiplePermission() {
        permissionHelper
                .with(this)
                .askFor(PermissionEnum.CALENDAR, PermissionEnum.CAMERA, PermissionEnum.CONTACTS,
                        PermissionEnum.LOCATION, PermissionEnum.MICROPHONE, PermissionEnum.STORAGE,
                        PermissionEnum.PHONE, PermissionEnum.SMS, PermissionEnum.SENSORS)
                .rationalMessage("Permissions are required for app to work properly")
                .requestCode(REQUEST_CODE_MULTIPLE)
                .setPermissionResultCallback(this)
                .build();
    }


    private void openPermissionSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
