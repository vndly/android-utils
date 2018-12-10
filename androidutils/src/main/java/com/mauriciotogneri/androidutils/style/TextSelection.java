package com.mauriciotogneri.androidutils.style;

import android.support.annotation.ColorInt;

import com.mauriciotogneri.androidutils.style.styles.BoldStyle;
import com.mauriciotogneri.androidutils.style.styles.ClickableStyle;
import com.mauriciotogneri.androidutils.style.styles.ClickableStyle.ClickableStyleCallback;
import com.mauriciotogneri.androidutils.style.styles.ColorStyle;
import com.mauriciotogneri.androidutils.style.styles.ItalicStyle;
import com.mauriciotogneri.androidutils.style.styles.SizeStyle;
import com.mauriciotogneri.androidutils.style.styles.StrikethroughStyle;
import com.mauriciotogneri.androidutils.style.styles.UnderlineStyle;

import java.util.ArrayList;
import java.util.List;

public class TextSelection
{
    private final String pattern;
    private final Integer color;
    private final Float size;
    private final Boolean bold;
    private final Boolean italic;
    private final Boolean underline;
    private final Boolean strikethrough;
    private final ClickableStyleCallback callback;

    public TextSelection(String pattern,
                         @ColorInt Integer color,
                         Float size,
                         Boolean bold,
                         Boolean italic,
                         Boolean underline,
                         Boolean strikethrough,
                         ClickableStyleCallback callback)
    {
        this.pattern = pattern;
        this.color = color;
        this.size = size;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.strikethrough = strikethrough;
        this.callback = callback;
    }

    public TextSelection(String pattern)
    {
        this(pattern, null, null, null, null, null, null, null);
    }

    public List<Object> spans(String text)
    {
        List<Object> spans = new ArrayList<>();

        if (callback != null)
        {
            spans.add(new ClickableStyle(callback, text));
        }

        if (color != null)
        {
            spans.add(new ColorStyle(color));
        }

        if (size != null)
        {
            spans.add(new SizeStyle(size));
        }

        if (bold != null)
        {
            spans.add(new BoldStyle());
        }

        if (italic != null)
        {
            spans.add(new ItalicStyle());
        }

        if (underline != null)
        {
            spans.add(new UnderlineStyle());
        }

        if (strikethrough != null)
        {
            spans.add(new StrikethroughStyle());
        }

        return spans;
    }

    public String pattern()
    {
        return pattern;
    }

    public TextSelection color(@ColorInt int color)
    {
        return new TextSelection(pattern, color, size, bold, italic, underline, strikethrough, callback);
    }

    public TextSelection size(float size)
    {
        return new TextSelection(pattern, color, size, bold, italic, underline, strikethrough, callback);
    }

    public TextSelection bold()
    {
        return new TextSelection(pattern, color, size, true, italic, underline, strikethrough, callback);
    }

    public TextSelection italic()
    {
        return new TextSelection(pattern, color, size, bold, true, underline, strikethrough, callback);
    }

    public TextSelection underline()
    {
        return new TextSelection(pattern, color, size, bold, italic, true, strikethrough, callback);
    }

    public TextSelection strikethrough()
    {
        return new TextSelection(pattern, color, size, bold, italic, underline, true, callback);
    }

    public TextSelection callback(ClickableStyleCallback callback)
    {
        return new TextSelection(pattern, color, size, bold, italic, underline, strikethrough, callback);
    }
}