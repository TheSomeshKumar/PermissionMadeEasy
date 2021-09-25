[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PermissionMadeEasy-blue.svg?style=flat)](https://android-arsenal.com/details/1/7335)
[![Release](https://jitpack.io/v//TheSomeshKumar/PermissionMadeEasy.svg)](https://jitpack.io/#TheSomeshKumar/PermissionMadeEasy)

## Note: Deprecated [as the new way of requesting permission](https://developer.android.com/training/permissions/requesting#allow-system-manage-request-code) is quite straightforward

PermissionMadeEasy
=======

Android Library for Easily calling Runtime Permission on Android Marshmallow and above

## How to build

Add Jitpack.io to your project level build.gradle file 
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
  
Add the dependency
```gradle
dependencies {
	  implementation 'com.github.thesomeshkumar:permissionmadeeasy:1.2.3'
	}
```
  
## How to use
  
Create a `PermissionHelper` object
  
```kotlin
permissionHelper = PermissionHelper.Builder()
        .with(this)
        .requestCode(REQUEST_CODE_MULTIPLE)
        .setPermissionResultCallback(this)
        .askFor(Permission.CALENDAR, Permission.CAMERA, Permission.CONTACTS,
                Permission.LOCATION, Permission.MICROPHONE, Permission.STORAGE,
                Permission.PHONE, Permission.SMS, Permission.SENSORS)
        .rationalMessage("Permissions are required for app to work properly")
        .build()
 ```
 and when you want to ask for the permission just call
 ```kotlin
permissionHelper.requestPermissions()
 ```
 
Override `onPermissionsGranted` and `onPermissionsDenied` functions

Also override `onRequestPermissionsResult` and pass the arguments recieved to `PermissionHelper` class' `onRequestPermissionsResult` function.
```kotlin
@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
```  

Detailed full sample project is included. Check [DemoActivity](https://github.com/thesomeshkumar/PermissionMadeEasy/blob/master/app/src/main/java/com/somesh/pemissionmadeeasy/DemoActivity.kt) for full implemetation 
