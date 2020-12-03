package com.mauriciotogneri.androidutils.style.styles;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class ClickableStyle extends ClickableSpan
{
    private final ClickableStyleCallback callback;
    private final String text;

    public ClickableStyle(ClickableStyleCallback callback, String text)
    {
        this.callback = callback;
        this.text = text;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds)
    {
    }

    @Override
    public void onClick(@NonNull View widget)
    {
        callback.onClick(text);
    }

    public interface ClickableStyleCallback
    {
        void onClick(String text);
    }
}