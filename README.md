PermissionMadeEasy
=======

Android Library for Easily calling Runtime Permission on Android Marshmallow and above

## How to build

Add Jitpack.io to your project.gradle file 
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
	  implementation 'com.github.someshkumar049:permissionmadeeasy:1.0'
	}
```
  
## How to use
  
Create a `PermissionHelper` object
  
```java
  PermissionHelper permissionHelper = new PermissionHelper();
  permissionHelper.with(this)
                .askFor(PermissionEnum.LOCATION)
                .rationalMessage("Permissions are required for app to work properly")
                .requestCode(REQUEST_CODE_SINGLE)
                .setPermissionResultCallback(this)
                .build();
 ```
Override `onPermissionsGranted` and `onPermissionsDenied` methods

Also override 

```java
@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
```  

Detailed full sample project is included. Check [DemoActivity](https://github.com/someshkumar049/PermissionMadeEasy/blob/master/app/src/main/java/com/somesh/pemissionmadeeasy/DemoActivity.java) for full implemetation 
