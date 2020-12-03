package com.mauriciotogneri.androidutils.base;

import androidx.fragment.app.FragmentManager;

public interface DialogDisplayer
{
    String string(int resId, Object... args);

    FragmentManager fragmentManager();
}