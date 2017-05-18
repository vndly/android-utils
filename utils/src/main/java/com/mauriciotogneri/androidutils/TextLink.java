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
    public static void format(TextView textView, String text, final @ColorInt int color, final LinkClickListener listener)
    {
        Pattern pattern = Pattern.compile("\\*.*\\*");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find())
        {
            int idx = matcher.start();
            String stringMatch = matcher.group();

            // remove * before and after
            final String link = stringMatch.substring(1, stringMatch.length() - 1);
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
                    textPaint.setColor(color);
                }
            };

            SpannableString spannable = new SpannableString(finalText);
            spannable.setSpan(clickableSpan, idx, idx + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(spannable);
        }
    }

    public interface LinkClickListener
    {
        void onClick(String text);
    }
}