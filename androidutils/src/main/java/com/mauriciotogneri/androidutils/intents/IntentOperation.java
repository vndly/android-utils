package com.mauriciotogneri.androidutils.intents;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class IntentOperation
{
    private final Intent intent;
    private final Integer requestCode;

    public IntentOperation(Intent intent, Integer requestCode)
    {
        this.intent = intent;
        this.requestCode = requestCode;
    }

    public IntentOperation(Intent intent)
    {
        this(intent, null);
    }

    public IntentOperation requestCode(int requestCode)
    {
        return new IntentOperation(intent, requestCode);
    }

    public void send(Activity activity)
    {
        send(new IntentTarget()
        {
            @Override
            public Context context()
            {
                return activity;
            }

            @Override
            public void startActivity(Intent intent)
            {
                activity.startActivity(intent);
            }

            @Override
            public void startActivityForResult(Intent intent, int requestCode)
            {
                activity.startActivityForResult(intent, requestCode);
            }
        });
    }

    public void send(Fragment fragment)
    {
        send(new IntentTarget()
        {
            @Override
            public Context context()
            {
                return fragment.getContext();
            }

            @Override
            public void startActivity(Intent intent)
            {
                fragment.startActivity(intent);
            }

            @Override
            public void startActivityForResult(Intent intent, int requestCode)
            {
                fragment.startActivityForResult(intent, requestCode);
            }
        });
    }

    public void send(IntentTarget target)
    {
        if (requestCode == null)
        {
            startActivity(target, intent);
        }
        else
        {
            startActivityForResult(target, intent, requestCode);
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void startActivity(@NonNull IntentTarget target, @NonNull Intent intent)
    {
        target.startActivity(intent);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void startActivityForResult(@NonNull IntentTarget target, @NonNull Intent intent, int requestCode)
    {
        target.startActivityForResult(intent, requestCode);
    }
}