package com.mauriciotogneri.androidutils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.net.URLConnection;

public class MimeType
{
    private MimeType()
    {
    }

    public static String from(File file, String defaultValue)
    {
        try
        {
            return URLConnection.guessContentTypeFromName(file.getAbsolutePath());
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
            String extension = MimeTypeMap.getFileExtensionFromUrl(uri.getPath());

            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        catch (Exception e1)
        {
            try
            {
                ContentResolver contentResolver = context.getContentResolver();

                return contentResolver.getType(uri);
            }
            catch (Exception e2)
            {
                return defaultValue;
            }
        }
    }
}