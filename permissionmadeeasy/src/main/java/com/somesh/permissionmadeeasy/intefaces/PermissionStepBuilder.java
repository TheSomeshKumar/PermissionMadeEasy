package com.somesh.permissionmadeeasy.intefaces;

import com.somesh.permissionmadeeasy.enums.PermissionEnum;

import android.app.Activity;
import android.app.Fragment;

/**
 * If this code works, it was written by Somesh Kumar on 24, December 2017. If not, I don’t know who wrote it.
 */
public class PermissionStepBuilder {
    private PermissionStepBuilder() {
    }

    public static StepOne with(Activity activity) {
        return new Steps();
    }

    public static StepOne with(Fragment activity) {
        return new Steps();
    }


    public static interface StepOne {
        StepTwo requestCode(int requestCode);
    }

    public static interface StepTwo {
        public StepThree askFor(PermissionEnum... permissionEnum);
    }

    public static interface StepThree {
        public LastStep rationMessage(String rationMessage);
        public Steps build();
    }


    public static interface LastStep {
        public Steps build();
    }

    private static class Steps implements StepOne, StepTwo, StepThree, LastStep {

        private String name;
        private String host;
        private String user;
        private String password;

        public LastStep onLocalhost() {
            this.host = "localhost";
            return this;
        }

        public StepTwo name(String name) {
            this.name = name;
            return null;
        }

        public StepThree onRemotehost(String host) {
            this.host = host;
            return this;
        }

        public LastStep credentials(String user, String password) {
            this.user = user;
            this.password = password;
            return this;
        }

        public Steps build() {
            return this;
        }

        @Override
        public StepTwo requestCode(int requestCode) {
            return null;
        }

        @Override
        public StepThree askFor(PermissionEnum... permissionEnum) {
            return null;
        }

        @Override
        public LastStep rationMessage(String rationMessage) {
            return null;
        }
    }
}
