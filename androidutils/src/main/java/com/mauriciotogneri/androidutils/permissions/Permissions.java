package com.mauriciotogneri.androidutils.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions
{
    private final Object target;
    private final PermissionRequest permissionRequest;

    public Permissions(Object target, final Activity activity)
    {
        this.target = target;
        this.permissionRequest = new PermissionRequest()
        {
            @Override
            public Context context()
            {
                return activity.getApplicationContext();
            }

            @Override
            public void request(int requestCode, String... permissions)
            {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }
        };
    }

    public Permissions(Object target, final Fragment fragment)
    {
        this.target = target;
        this.permissionRequest = new PermissionRequest()
        {
            @Override
            public Context context()
            {
                return fragment.getContext().getApplicationContext();
            }

            @Override
            public void request(int requestCode, String... permissions)
            {
                fragment.requestPermissions(permissions, requestCode);
            }
        };
    }

    public void request(int requestCode, String... permissions)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            String[] permissionsGranted = permissionsWithStatus(permissionRequest.context(), PackageManager.PERMISSION_GRANTED, permissions);

            if (permissionsGranted.length > 0)
            {
                grantPermissions(target, requestCode, permissionsGranted);
            }

            String[] permissionsNotGranted = permissionsWithStatus(permissionRequest.context(), PackageManager.PERMISSION_DENIED, permissions);

            if (permissionsNotGranted.length > 0)
            {
                permissionRequest.request(requestCode, permissionsNotGranted);
            }
        }
        else
        {
            grantPermissions(target, requestCode, permissions);
        }
    }

    private void grantPermissions(Object target, int requestCode, String... permissions)
    {
        PermissionsResult permissionsResult = new PermissionsResult(target, requestCode, permissions, new int[permissions.length]);
        permissionsResult.process();
    }

    private String[] permissionsWithStatus(Context context, int status, String... permissions)
    {
        List<String> result = new ArrayList<>();

        for (String permission : permissions)
        {
            if (ContextCompat.checkSelfPermission(context, permission) == status)
            {
                result.add(permission);
            }
        }

        String[] list = new String[result.size()];
        result.toArray(list);

        return list;
    }

    private interface PermissionRequest
    {
        Context context();

        void request(int requestCode, String... permissions);
    }
}