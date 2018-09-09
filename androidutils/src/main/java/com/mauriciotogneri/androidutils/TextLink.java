package com.mauriciotogneri.androidutils;

import android.support.annotation.ColorInt;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.UpdateAppearance;
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

            Matcher matcher = Pattern.compile(textSection.pattern).matcher(text);

            if (matcher.find())
            {
                int startIndex = matcher.start() - extra;

                String stringMatched = matcher.group();
                String link = stringMatched.substring(1, stringMatched.length() - 1);

                extra += (stringMatched.length() - link.length());

                CharacterStyle span;

                if (textSection.callback != null)
                {
                    span = clickableSpan(textSection, link);
                }
                else
                {
                    span = normalSpan(textSection);
                }

                spannable.setSpan(span, startIndex, startIndex + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        target.setMovementMethod(LinkMovementMethod.getInstance());
        target.setText(spannable);
    }

    private CharacterStyle normalSpan(TextSection textSection)
    {
        return new NormalSpan()
        {
            @Override
            public void updateDrawState(TextPaint textPaint)
            {
                if (textSection.color != null)
                {
                    textPaint.setColor(textSection.color);
                }

                if (textSection.bold != null)
                {
                    textPaint.setFakeBoldText(textSection.bold);
                }

                if (textSection.underline != null)
                {
                    textPaint.setUnderlineText(textSection.underline);
                }

                if (textSection.size != null)
                {
                    textPaint.setTextSize(textSection.size);
                }
            }
        };
    }

    private CharacterStyle clickableSpan(TextSection textSection, String link)
    {
        return new ClickableSpan()
        {
            @Override
            public void onClick(View textView)
            {
                textSection.callback.onClick(link);
            }

            @Override
            public void updateDrawState(TextPaint textPaint)
            {
                if (textSection.color != null)
                {
                    textPaint.setColor(textSection.color);
                }

                if (textSection.bold != null)
                {
                    textPaint.setFakeBoldText(textSection.bold);
                }

                if (textSection.underline != null)
                {
                    textPaint.setUnderlineText(textSection.underline);
                }

                if (textSection.size != null)
                {
                    textPaint.setTextSize(textSection.size);
                }
            }
        };
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

        public TextSection(String pattern, @ColorInt Integer color, Integer size, Boolean bold, Boolean underline, LinkClickCallback callback)
        {
            this.pattern = pattern;
            this.color = color;
            this.size = size;
            this.bold = bold;
            this.underline = underline;
            this.callback = callback;
        }

        public TextSection(String pattern)
        {
            this(pattern, null, null, null, null, null);
        }

        public TextSection callback(LinkClickCallback callback)
        {
            return new TextSection(pattern, color, size, bold, underline, callback);
        }

        public TextSection color(@ColorInt int color)
        {
            return new TextSection(pattern, color, size, bold, underline, callback);
        }

        public TextSection size(int size)
        {
            return new TextSection(pattern, color, size, bold, underline, callback);
        }

        public TextSection bold(boolean bold)
        {
            return new TextSection(pattern, color, size, bold, underline, callback);
        }

        public TextSection underline(boolean underline)
        {
            return new TextSection(pattern, color, size, bold, underline, callback);
        }
    }

    public static class NormalSpan extends CharacterStyle implements UpdateAppearance
    {
        @Override
        public void updateDrawState(TextPaint ds)
        {
        }
    }
}