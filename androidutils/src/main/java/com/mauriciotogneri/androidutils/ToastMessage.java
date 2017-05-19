package com.mauriciotogneri.androidutils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastMessage
{
    private final Context context;

    public ToastMessage(Context context)
    {
        this.context = context;
    }

    public void shortMessage(@StringRes int stringId, Object... arguments)
    {
        display(context.getString(stringId, arguments), Toast.LENGTH_SHORT);
    }

    public void shortMessage(String string, Object... arguments)
    {
        display(String.format(string, arguments), Toast.LENGTH_SHORT);
    }

    public void longMessage(@StringRes int stringId, Object... arguments)
    {
        display(context.getString(stringId, arguments), Toast.LENGTH_LONG);
    }

    public void longMessage(String string, Object... arguments)
    {
        display(String.format(string, arguments), Toast.LENGTH_LONG);
    }

    public Toast toast(String text, int type)
    {
        return Toast.makeText(context, text, type);
    }

    public Toast toast(@StringRes int stringId, int type)
    {
        return Toast.makeText(context, stringId, type);
    }

    private void display(String string, int type)
    {
        toast(string, type).show();
    }
}