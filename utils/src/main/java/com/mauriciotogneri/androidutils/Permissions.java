package com.mauriciotogneri.androidutils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions
{
    private final Activity activity;

    public Permissions(Activity activity)
    {
        this.activity = activity;
    }

    public boolean request(int requestCode, String... permissions)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            String[] permissionsNotGranted = permissionsNotGranted(permissions);

            if (permissionsNotGranted.length > 0)
            {
                ActivityCompat.requestPermissions(activity, permissionsNotGranted, requestCode);

                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return true;
        }
    }

    private String[] permissionsNotGranted(String... permissions)
    {
        List<String> result = new ArrayList<>();

        for (String permission : permissions)
        {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
            {
                result.add(permission);
            }
        }

        String[] list = new String[result.size()];
        result.toArray(list);

        return list;
    }

    public static class PermissionsResult
    {
        private final String permissions[];
        private final int[] grantResults;

        public PermissionsResult(String[] permissions, int[] grantResults)
        {
            this.permissions = permissions;
            this.grantResults = grantResults;
        }

        public boolean allGranted()
        {
            for (int result : grantResults)
            {
                if (result != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }

            return true;
        }

        public boolean isGranted(String permission)
        {
            for (int i = 0; i < permissions.length; i++)
            {
                if (permissions[i].equals(permission))
                {
                    return grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            }

            return false;
        }
    }
}