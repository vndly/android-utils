package com.mauriciotogneri.androidutils;

import android.support.annotation.ColorInt;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextLink
{
    private final TextView target;
    private final String text;
    private final List<String> patterns;
    private final List<TextSection> sections;

    public TextLink(TextView target, String text)
    {
        this.target = target;
        this.text = text;
        this.patterns = new ArrayList<>();
        this.sections = new ArrayList<>();
    }

    public void add(TextSection textSection)
    {
        this.patterns.add(textSection.pattern);
        this.sections.add(textSection);
    }

    public void format()
    {
        int extra = 0;
        SpannableString spannable = new SpannableString(finalText());

        for (int i = 0; i < sections.size(); i++)
        {
            TextSection textSection = sections.get(i);

            String pattern = textSection.pattern;
            LinkClickCallback callback = textSection.callback;
            Integer color = textSection.color;
            Integer size = textSection.size;
            Boolean bold = textSection.bold;
            Boolean underline = textSection.underline;

            Matcher matcher = Pattern.compile(pattern).matcher(text);

            if (matcher.find())
            {
                int startIndex = matcher.start() - extra;

                String stringMatched = matcher.group();
                String link = stringMatched.substring(1, stringMatched.length() - 1);

                extra += (stringMatched.length() - link.length());

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

                        if (bold != null)
                        {
                            textPaint.setFakeBoldText(bold);
                        }

                        if (underline != null)
                        {
                            textPaint.setUnderlineText(underline);
                        }

                        if (size != null)
                        {
                            textPaint.setTextSize(size);
                        }
                    }
                };

                spannable.setSpan(clickableSpan, startIndex, startIndex + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        target.setMovementMethod(LinkMovementMethod.getInstance());
        target.setText(spannable);
    }

    private String finalText()
    {
        String result = text;

        for (String pattern : patterns)
        {
            Matcher matcher = Pattern.compile(pattern).matcher(text);

            if (matcher.find())
            {
                String stringMatched = matcher.group();
                String link = stringMatched.substring(1, stringMatched.length() - 1);

                result = result.replace(stringMatched, link);
            }
        }

        return result;
    }

    public interface LinkClickCallback
    {
        void onClick(String text);
    }

    public static class TextSection
    {
        private final String pattern;
        private final LinkClickCallback callback;
        private final Integer color;
        private final Integer size;
        private final Boolean bold;
        private final Boolean underline;

        public TextSection(String pattern, LinkClickCallback callback, @ColorInt Integer color, Integer size, Boolean bold, Boolean underline)
        {
            this.pattern = pattern;
            this.callback = callback;
            this.color = color;
            this.size = size;
            this.bold = bold;
            this.underline = underline;
        }

        public TextSection(String pattern, LinkClickCallback callback)
        {
            this(pattern, callback, null, null, null, null);
        }

        public TextSection color(@ColorInt int color)
        {
            return new TextSection(pattern, callback, color, size, bold, underline);
        }

        public TextSection size(int size)
        {
            return new TextSection(pattern, callback, color, size, bold, underline);
        }

        public TextSection bold(boolean bold)
        {
            return new TextSection(pattern, callback, color, size, bold, underline);
        }

        public TextSection underline(boolean underline)
        {
            return new TextSection(pattern, callback, color, size, bold, underline);
        }
    }
}