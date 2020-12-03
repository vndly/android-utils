package com.mauriciotogneri.androidutils.style.styles;

import android.text.TextPaint;
import android.text.style.CharacterStyle;

import androidx.annotation.NonNull;

public class SizeStyle extends CharacterStyle
{
    private final float size;

    public SizeStyle(float size)
    {
        this.size = size;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint tp)
    {
        tp.setTextSize(size);
    }
}