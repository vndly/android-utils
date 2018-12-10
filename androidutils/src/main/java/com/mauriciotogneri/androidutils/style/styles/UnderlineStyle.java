package com.mauriciotogneri.androidutils.style.styles;

import android.text.TextPaint;
import android.text.style.CharacterStyle;

public class UnderlineStyle extends CharacterStyle
{
    @Override
    public void updateDrawState(TextPaint tp)
    {
        tp.setUnderlineText(true);
    }
}