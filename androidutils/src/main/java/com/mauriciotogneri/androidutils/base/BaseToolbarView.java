package com.mauriciotogneri.androidutils.base;

import android.view.View;

import com.mauriciotogneri.androidutils.uibinder.UiBinder;

public abstract class BaseToolbarView<O, C>
{
    protected final O observer;
    protected final C ui;

    protected BaseToolbarView(O observer, C ui)
    {
        this.observer = observer;
        this.ui = ui;
    }

    public void initialize(View view)
    {
        UiBinder uiBinder = new UiBinder();
        uiBinder.bind(view, ui);
    }
}