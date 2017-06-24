package com.mauriciotogneri.androidutils.permissions;

import android.content.pm.PackageManager;

import java.lang.reflect.Method;

public class PermissionsResult
{
    private final Object target;
    private final int requestCode;
    private final String permissions[];
    private final int[] grantResults;

    public PermissionsResult(Object target, int requestCode, String[] permissions, int[] grantResults)
    {
        this.target = target;
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.grantResults = grantResults;
    }

    public void process()
    {
        for (int i = 0; i < grantResults.length; i++)
        {
            callTarget(target, requestCode, permissions[i], grantResults[i]);
        }
    }

    private void callTarget(Object target, int requestCode, String permission, int status)
    {
        for (Method method : target.getClass().getMethods())
        {
            if (valid(method, requestCode, permission, status))
            {
                try
                {
                    method.invoke(target);
                    break;
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private boolean valid(Method method, int requestCode, String permission, int status)
    {
        if (status == PackageManager.PERMISSION_GRANTED)
        {
            OnPermissionGranted onPermissionGranted = method.getAnnotation(OnPermissionGranted.class);

            return ((onPermissionGranted != null) && (onPermissionGranted.requestCode() == requestCode) && (onPermissionGranted.permission().equals(permission)));
        }
        else if (status == PackageManager.PERMISSION_DENIED)
        {
            OnPermissionDenied onPermissionDenied = method.getAnnotation(OnPermissionDenied.class);

            return ((onPermissionDenied != null) && (onPermissionDenied.requestCode() == requestCode) && (onPermissionDenied.permission().equals(permission)));
        }
        else
        {
            return false;
        }
    }
}