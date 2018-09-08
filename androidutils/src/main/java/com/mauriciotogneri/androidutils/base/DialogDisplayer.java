package com.mauriciotogneri.androidutils.base;

import android.support.v4.app.FragmentManager;

public interface DialogDisplayer
{
    String string(int resId, Object... args);

    FragmentManager fragmentManager();
}