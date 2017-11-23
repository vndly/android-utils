package com.mauriciotogneri.androidutils;

import android.util.Base64;

public class Encoding
{
    public static String toBase64(byte[] bytes)
    {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static byte[] fromBase64(String text)
    {
        return Base64.decode(text, Base64.DEFAULT);
    }
}