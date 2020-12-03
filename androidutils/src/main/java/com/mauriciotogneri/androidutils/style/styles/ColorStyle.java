package com.mauriciotogneri.androidutils.style.styles;

import android.text.TextPaint;
import android.text.style.CharacterStyle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public class ColorStyle extends CharacterStyle
{
    private final int color;

    public ColorStyle(@ColorInt int color)
    {
        this.color = color;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint tp)
    {
        tp.setColor(color);
    }
}