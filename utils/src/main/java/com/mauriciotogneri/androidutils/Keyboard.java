package com.mauriciotogneri.androidutils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Keyboard
{
    private final InputMethodManager inputMethodManager;

    public Keyboard(Context context)
    {
        this.inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void show(View view)
    {
        view.requestFocus();

        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public void hide(View view)
    {
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}