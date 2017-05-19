package com.mauriciotogneri.androidutils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class FormattedDateTime
{
    private final DateTimeFormatter formatter;

    public FormattedDateTime(DateTimeFormatter formatter)
    {
        this.formatter = formatter;
    }

    public FormattedDateTime(String pattern)
    {
        this(DateTimeFormat.forPattern(pattern));
    }

    public FormattedDateTime()
    {
        this("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    // ============================================================================================

    public String date(DateTime dateTime, DateTimeZone timeZone, Locale locale, String defaultValue)
    {
        if ((dateTime != null) && (timeZone != null) && (locale != null))
        {
            return formatter.withZone(timeZone).withLocale(locale).print(dateTime);
        }
        else
        {
            return defaultValue;
        }
    }

    public String date(DateTime dateTime, Locale locale, String defaultValue)
    {
        return date(dateTime, DateTimeZone.getDefault(), locale, defaultValue);
    }

    public String date(DateTime dateTime, DateTimeZone timeZone, Locale locale)
    {
        return date(dateTime, timeZone, locale, null);
    }

    public String date(DateTime dateTime, Locale locale)
    {
        return date(dateTime, DateTimeZone.getDefault(), locale, null);
    }

    // ============================================================================================

    public DateTime date(String timestamp, DateTimeZone timeZone, DateTime defaultValue)
    {
        try
        {
            return formatter.withZone(timeZone).parseDateTime(timestamp);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    public DateTime date(String timestamp, DateTime defaultValue)
    {
        return date(timestamp, DateTimeZone.getDefault(), defaultValue);
    }

    public DateTime date(String timestamp, DateTimeZone timeZone)
    {
        return date(timestamp, timeZone, null);
    }

    public DateTime date(String timestamp)
    {
        return date(timestamp, DateTimeZone.getDefault(), null);
    }
}