package com.mauriciotogneri.androidutils.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public abstract class BaseCustomDialog<V extends BaseView, C> extends BaseDefaultDialog<C>
{
    protected V view;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        view = view();
    }

    protected boolean isDialogCancelable()
    {
        return false;
    }

    protected void setupWindow(Window window)
    {
    }

    protected abstract V view();

    protected abstract void initialize();

    protected abstract int style();

    @NonNull
    @Override
    @SuppressWarnings("ConstantConditions")
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View layout = view.inflate(LayoutInflater.from(getContext()), null);
        initialize();

        Dialog dialog = new Dialog(getContext(), style());
        dialog.setContentView(layout);
        dialog.setCancelable(isDialogCancelable());
        dialog.setCanceledOnTouchOutside(isDialogCancelable());

        setupWindow(dialog.getWindow());

        return dialog;
    }
}