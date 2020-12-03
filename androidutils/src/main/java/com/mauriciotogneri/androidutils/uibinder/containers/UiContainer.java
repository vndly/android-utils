package com.mauriciotogneri.androidutils.uibinder.containers;

import android.view.View;

import androidx.annotation.IdRes;

public interface UiContainer
{
    View findViewById(@IdRes int id);
}