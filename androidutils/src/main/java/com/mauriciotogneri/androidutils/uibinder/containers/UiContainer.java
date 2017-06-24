package com.mauriciotogneri.androidutils.uibinder.containers;

import android.support.annotation.IdRes;
import android.view.View;

public interface UiContainer
{
    View findViewById(@IdRes int id);
}