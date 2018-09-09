/*
 * (c) Copyright Banque Pictet & Cie SA
 */

package com.mauriciotogneri.androidutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.DimenRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public abstract class Fonts
{
    private static final Map<String, FontType> fonts = new HashMap<>();

    private final Context context;

    public Fonts(Context context)
    {
        this.context = context;

        if (fonts.isEmpty())
        {
            loadFonts();
        }
    }

    protected abstract void loadFonts();

    protected abstract String defaultFontName();

    protected Typeface loadTypeface(String fileName)
    {
        return Typeface.createFromAsset(context.getAssets(), String.format("fonts/%s", fileName));
    }

    protected void loadFont(String key, Typeface typeface, int unit, @DimenRes int sizeId)
    {
        FontType fontType = new FontType(typeface, unit, context.getResources().getDimension(sizeId));

        fonts.put(key, fontType);
    }

    protected void loadFont(String key, Typeface typeface, int unit, float size)
    {
        FontType fontType = new FontType(typeface, unit, size);

        fonts.put(key, fontType);
    }

    protected void loadFont(String key, Typeface typeface)
    {
        FontType fontType = new FontType(typeface);

        fonts.put(key, fontType);
    }

    private FontType defaultFont()
    {
        return fonts.get(defaultFontName());
    }

    public void apply(TextView textView, Context context, AttributeSet attrs, int fontStyle, int[] widgetStyle)
    {
        if (attrs != null)
        {
            TypedArray styledAttributes = context.obtainStyledAttributes(attrs, widgetStyle);
            String fontName = styledAttributes.getString(fontStyle);
            styledAttributes.recycle();

            if (!TextUtils.isEmpty(fontName))
            {
                apply(textView, font(fontName));
            }
            else
            {
                apply(textView, defaultFont());
            }
        }
        else
        {
            apply(textView, defaultFont());
        }
    }

    private void apply(TextView textView, FontType fontType)
    {
        textView.setTypeface(fontType.typeface);

        if (fontType.hasSize())
        {
            textView.setTextSize(fontType.unit, fontType.size);
        }
    }

    private FontType font(String name)
    {
        FontType fontType = fonts.get(name);

        return (fontType != null) ? fontType : defaultFont();
    }

    public static class FontType
    {
        private final Typeface typeface;
        private final Integer unit;
        private final Float size;

        private FontType(Typeface typeface, Integer unit, Float size)
        {
            this.typeface = typeface;
            this.unit = unit;
            this.size = size;
        }

        private FontType(Typeface typeface)
        {
            this(typeface, null, null);
        }

        public boolean hasSize()
        {
            return (unit != null) && (size != null);
        }
    }
}