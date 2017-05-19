package com.mauriciotogneri.androidutils;

import android.text.TextUtils;

public class EnumValue
{
    public static <T extends Enum<T>> T from(Class<T> clazz, String text, T defaultValue)
    {
        for (T value : clazz.getEnumConstants())
        {
            if (TextUtils.equals(value.toString(), text))
            {
                return value;
            }
        }

        return defaultValue;
    }

    public static <T extends Enum<T>> T from(Class<T> clazz, String text)
    {
        return from(clazz, text, null);
    }
}