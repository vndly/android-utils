package com.mauriciotogneri.androidutils.style.styles;

import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.text.style.CharacterStyle;

public class ColorStyle extends CharacterStyle
{
    private final int color;

    public ColorStyle(@ColorInt int color)
    {
        this.color = color;
    }

    @Override
    public void updateDrawState(TextPaint tp)
    {
        tp.setColor(color);
    }
}