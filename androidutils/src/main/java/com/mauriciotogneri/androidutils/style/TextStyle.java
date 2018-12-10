package com.mauriciotogneri.androidutils.style;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.SparseArray;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextStyle
{
    private final TextView target;
    private final String text;
    private final List<TextSelection> selections;

    public TextStyle(TextView target, String text)
    {
        this.target = target;
        this.text = text;
        this.selections = new ArrayList<>();
    }

    public TextStyle(TextView target)
    {
        this.target = target;
        this.text = target.getText().toString();
        this.selections = new ArrayList<>();
    }

    public void add(TextSelection selection)
    {
        this.selections.add(selection);
    }

    public void format()
    {
        String result = text;
        List<TextStyleMatch> matches = new ArrayList<>();
        List<TextSelection> orderedSelections = orderedSelections(text, selections);

        for (TextSelection selection : orderedSelections)
        {
            Matcher matcher = Pattern.compile(selection.pattern()).matcher(text);

            if (matcher.find())
            {
                String withPattern = matcher.group(0);
                String withoutPattern = matcher.group(1);
                int start = result.indexOf(withPattern);
                int end = start + withoutPattern.length();

                matches.add(new TextStyleMatch(start, end, selection.spans(withoutPattern)));

                result = result.replace(withPattern, withoutPattern);
            }
        }

        SpannableString spannable = new SpannableString(result);

        for (TextStyleMatch match : matches)
        {
            for (Object span : match.spans)
            {
                spannable.setSpan(span, match.start, match.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        target.setMovementMethod(LinkMovementMethod.getInstance());
        target.setText(spannable);
    }

    private List<TextSelection> orderedSelections(String text, List<TextSelection> selections)
    {
        SparseArray<TextSelection> array = new SparseArray<>();

        for (TextSelection selection : selections)
        {
            Matcher matcher = Pattern.compile(selection.pattern()).matcher(text);

            if (matcher.find())
            {
                array.put(matcher.start(), selection);
            }
        }

        List<TextSelection> result = new ArrayList<>();

        for (int i = 0; i < array.size(); i++)
        {
            result.add(array.valueAt(i));
        }

        return result;
    }

    private static class TextStyleMatch
    {
        public final int start;
        public final int end;
        public final List<Object> spans;

        private TextStyleMatch(int start, int end, List<Object> spans)
        {
            this.start = start;
            this.end = end;
            this.spans = spans;
        }
    }
}