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
    private final String pattern;

    public TextLink(String pattern)
    {
        this.pattern = pattern;
    }

    public void format(TextView textView, String text, final @ColorInt int color, final boolean underline, final LinkClickListener listener)
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
                    listener.onClick(link);
                }

                @Override
                public void updateDrawState(TextPaint textPaint)
                {
                    textPaint.setUnderlineText(underline);
                    textPaint.setColor(color);
                }
            };

            SpannableString spannable = new SpannableString(finalText);
            spannable.setSpan(clickableSpan, startIndex, startIndex + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(spannable);
        }
    }

    public interface LinkClickListener
    {
        void onClick(String text);
    }
}