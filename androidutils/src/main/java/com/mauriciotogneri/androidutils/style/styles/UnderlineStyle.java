package com.mauriciotogneri.androidutils.style.styles;

import android.text.TextPaint;
import android.text.style.CharacterStyle;

import androidx.annotation.NonNull;

public class UnderlineStyle extends CharacterStyle
{
    @Override
    public void updateDrawState(@NonNull TextPaint tp)
    {
        tp.setUnderlineText(true);
    }
}