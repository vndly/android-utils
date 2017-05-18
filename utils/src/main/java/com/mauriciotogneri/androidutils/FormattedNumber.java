package com.mauriciotogneri.androidutils;

import java.text.NumberFormat;
import java.util.Locale;

public class FormattedNumber
{
    private final Locale locale;
    private final Integer minDecimals;
    private final Integer maxDecimals;
    private final Boolean withSign;

    private FormattedNumber(Locale locale, Integer minDecimals, Integer maxDecimals, Boolean withSign)
    {
        this.locale = locale;
        this.minDecimals = minDecimals;
        this.maxDecimals = maxDecimals;
        this.withSign = withSign;
    }

    public String format(double value)
    {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);

        if (minDecimals != null)
        {
            numberFormat.setMinimumFractionDigits(minDecimals);
        }

        if (maxDecimals != null)
        {
            numberFormat.setMaximumFractionDigits(maxDecimals);
        }

        if ((withSign != null) && (withSign))
        {
            return String.format("+%s", numberFormat.format(value));
        }
        else
        {
            return numberFormat.format(value);
        }
    }

    public static class Builder
    {
        private final Locale locale;
        private Integer minDecimals;
        private Integer maxDecimals;
        private Boolean withSign;

        public Builder(Locale locale)
        {
            this.locale = locale;
        }

        public void minDecimals(Integer minDecimals)
        {
            this.minDecimals = minDecimals;
        }

        public void maxDecimals(Integer maxDecimals)
        {
            this.maxDecimals = maxDecimals;
        }

        public void withSign(Boolean withSign)
        {
            this.withSign = withSign;
        }

        public FormattedNumber build()
        {
            return new FormattedNumber(locale, minDecimals, maxDecimals, withSign);
        }
    }
}