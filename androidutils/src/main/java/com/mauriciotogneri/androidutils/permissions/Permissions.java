package com.mauriciotogneri.androidutils.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.mauriciotogneri.javautils.Lists;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class Permissions
{
    private final Object target;
    private final PermissionRequest permissionRequest;

    public Permissions(Object target, Activity activity)
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

    public Permissions(Object target, Fragment fragment)
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
        PermissionsResult permissionsResult = new PermissionsResult(target);
        permissionsResult.process(requestCode, permissions, new int[permissions.length]);
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

        return Lists.asArray(result, String.class);
    }

    public static boolean isGranted(Context context, String... permissions)
    {
        return checkPermission(context, PackageManager.PERMISSION_GRANTED, permissions);
    }

    public static boolean isDenied(Context context, String... permissions)
    {
        return checkPermission(context, PackageManager.PERMISSION_DENIED, permissions);
    }

    private static boolean checkPermission(Context context, int status, String... permissions)
    {
        boolean granted = true;

        for (String permission : permissions)
        {
            granted &= (ContextCompat.checkSelfPermission(context, permission) == status);
        }

        return granted;
    }

    private interface PermissionRequest
    {
        Context context();

        void request(int requestCode, String... permissions);
    }
}