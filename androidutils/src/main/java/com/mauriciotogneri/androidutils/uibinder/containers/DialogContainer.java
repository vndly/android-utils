package com.mauriciotogneri.androidutils.uibinder.containers;

import android.app.Dialog;
import android.support.annotation.IdRes;
import android.view.View;

public class DialogContainer implements UiContainer
{
    private final Dialog dialog;

    public DialogContainer(Dialog dialog)
    {
        this.dialog = dialog;
    }

    @Override
    public View findViewById(@IdRes int id)
    {
        return dialog.findViewById(id);
    }
}