package com.mauriciotogneri.androidutils.style.styles;

import android.text.TextPaint;
import android.text.style.CharacterStyle;

public class SizeStyle extends CharacterStyle
{
    private final float size;

    public SizeStyle(float size)
    {
        this.size = size;
    }

    @Override
    public void updateDrawState(TextPaint tp)
    {
        tp.setTextSize(size);
    }
}