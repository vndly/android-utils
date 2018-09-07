package com.mauriciotogneri.androidutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

public class ExitConfirmation
{
    private final Activity activity;
    private final Toast exitToast;

    @SuppressLint("ShowToast")
    public ExitConfirmation(Activity activity, @StringRes int textId)
    {
        this.activity = activity;
        this.exitToast = Toast.makeText(activity, textId, Toast.LENGTH_SHORT);
    }

    public void onPressBack()
    {
        if (isVisible())
        {
            display();
        }
        else
        {
            activity.finish();
        }
    }

    private boolean isVisible()
    {
        return (exitToast.getView().getWindowVisibility() != View.VISIBLE);
    }

    private void display()
    {
        exitToast.show();
    }
}