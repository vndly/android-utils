package com.mauriciotogneri.androidutils.style.styles;

import android.text.TextPaint;
import android.text.style.CharacterStyle;

public class StrikethroughStyle extends CharacterStyle
{
    @Override
    public void updateDrawState(TextPaint tp)
    {
        tp.setStrikeThruText(true);
    }
}