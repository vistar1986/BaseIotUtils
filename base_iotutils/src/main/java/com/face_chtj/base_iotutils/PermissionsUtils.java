package com.face_chtj.base_iotutils;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限申请工具类
 *     PermissionsUtils.with(mContext).
 *     addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
 *     addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
 *     addPermission(Manifest.permission......).//添加其他
 *     initPermission();
 */
public class PermissionsUtils {

    public static PermissionsUtils.Builder with(Activity activity) {
        return new Builder(activity);
    }

    public static class Builder {
        private Activity mActivity;
        private List<String> permissionList;

        public Builder(@NonNull Activity activity) {
            mActivity = activity;
            permissionList = new ArrayList<>();
        }

        /**
         * Determine whether <em>you</em> have been granted a particular permission.
         *
         * @param permission The name of the permission being checked.
         * @return {@link PackageManager#PERMISSION_GRANTED} if you have the
         * permission, or {@link PackageManager#PERMISSION_DENIED} if not.
         * @see PackageManager#checkPermission(String, String)
         */
        public Builder addPermission(@NonNull String permission) {
            if (!permissionList.contains(permission)) {
                permissionList.add(permission);
            }
            return this;
        }

        public List<String> initPermission() {
            List<String> list = new ArrayList<>();
            for (String permission : permissionList) {
                if (ActivityCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                    list.add(permission);
                }
            }
            if (list.size() > 0) {
                ActivityCompat.requestPermissions(mActivity, list.toArray(new String[list.size()]), 1);
            }
            return list;
        }
    }
}
