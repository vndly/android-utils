package com.mauriciotogneri.androidutils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

public class Keyboard
{
    private final InputMethodManager inputMethodManager;

    public Keyboard(@NonNull Context context)
    {
        this.inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void show(@NonNull View view)
    {
        view.requestFocus();

        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public void hide(@NonNull View view)
    {
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onChange(@NonNull View root, int threshold, KeyboardObserver observer)
    {
        root.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            root.getWindowVisibleDisplayFrame(rect);

            int heightDiff = root.getRootView().getHeight() - (rect.bottom - rect.top);

            observer.onChange((heightDiff > (root.getRootView().getHeight() / threshold)));
        });
    }

    public interface KeyboardObserver
    {
        void onChange(boolean opened);
    }
}