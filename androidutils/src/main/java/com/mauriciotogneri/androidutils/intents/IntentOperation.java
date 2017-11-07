package com.mauriciotogneri.androidutils.intents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

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

    public boolean send(final Activity activity)
    {
        return send(new IntentTarget()
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

    public boolean send(final Fragment fragment)
    {
        return send(new IntentTarget()
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

    public boolean send(IntentTarget target)
    {
        if (requestCode == null)
        {
            return startActivity(target, intent);
        }
        else
        {
            return startActivityForResult(target, intent, requestCode);
        }
    }

    private boolean startActivity(IntentTarget target, Intent intent)
    {
        if (intent.resolveActivity(target.context().getPackageManager()) != null)
        {
            target.startActivity(intent);

            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean startActivityForResult(IntentTarget target, Intent intent, int requestCode)
    {
        if (intent.resolveActivity(target.context().getPackageManager()) != null)
        {
            target.startActivityForResult(intent, requestCode);

            return true;
        }
        else
        {
            return false;
        }
    }
}