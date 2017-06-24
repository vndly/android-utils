package com.mauriciotogneri.androidutils;

import android.support.annotation.ColorInt;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextLink
{
    private final TextView target;
    private final String text;
    private final String pattern;
    private final Integer color;
    private final Boolean underline;
    private final Boolean bold;
    private final Integer size;
    private final LinkClickCallback callback;

    private TextLink(TextView target, String text, String pattern, Integer color, Boolean underline, Boolean bold, Integer size, LinkClickCallback callback)
    {
        this.target = target;
        this.text = text;
        this.pattern = pattern;
        this.color = color;
        this.underline = underline;
        this.bold = bold;
        this.size = size;
        this.callback = callback;
    }

    public TextLink(TextView target, String text, String pattern)
    {
        this(target, text, pattern, null, null, null, null, null);
    }

    public TextLink color(@ColorInt int color)
    {
        return new TextLink(target, text, pattern, color, underline, bold, size, callback);
    }

    public TextLink underline(Boolean underline)
    {
        return new TextLink(target, text, pattern, color, underline, bold, size, callback);
    }

    public TextLink bold(Boolean bold)
    {
        return new TextLink(target, text, pattern, color, underline, bold, size, callback);
    }

    public TextLink size(Integer size)
    {
        return new TextLink(target, text, pattern, color, underline, bold, size, callback);
    }

    public TextLink callback(LinkClickCallback callback)
    {
        return new TextLink(target, text, pattern, color, underline, bold, size, callback);
    }

    public void format()
    {
        Matcher matcher = Pattern.compile(pattern).matcher(text);

        if (matcher.find())
        {
            int startIndex = matcher.start();

            String stringMatched = matcher.group();
            final String link = stringMatched.substring(1, stringMatched.length() - 1);

            String finalText = matcher.replaceFirst(link);

            ClickableSpan clickableSpan = new ClickableSpan()
            {
                @Override
                public void onClick(View textView)
                {
                    if (callback != null)
                    {
                        callback.onClick(link);
                    }
                }

                @Override
                public void updateDrawState(TextPaint textPaint)
                {
                    if (color != null)
                    {
                        textPaint.setColor(color);
                    }

                    if (underline != null)
                    {
                        textPaint.setUnderlineText(underline);
                    }

                    if (bold != null)
                    {
                        textPaint.setFakeBoldText(bold);
                    }

                    if (size != null)
                    {
                        textPaint.setTextSize(size);
                    }
                }
            };

            SpannableString spannable = new SpannableString(finalText);
            spannable.setSpan(clickableSpan, startIndex, startIndex + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            target.setMovementMethod(LinkMovementMethod.getInstance());
            target.setText(spannable);
        }
    }

    public interface LinkClickCallback
    {
        void onClick(String text);
    }
}