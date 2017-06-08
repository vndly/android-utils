package com.mauriciotogneri.androidutils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

public class Extension
{
    private Extension()
    {
    }

    public static String from(File file, String defaultValue)
    {
        try
        {
            String name = file.getName();

            return name.substring(name.lastIndexOf(".") + 1, name.length());
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    public static String from(Context context, Uri uri, String defaultValue)
    {
        try
        {
            ContentResolver contentResolver = context.getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();

            return mime.getExtensionFromMimeType(contentResolver.getType(uri));
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }
}