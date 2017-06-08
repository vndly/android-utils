package com.mauriciotogneri.androidutils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Assets
{
    private final Context context;

    public Assets(Context context)
    {
        this.context = context;
    }

    public String read(String path) throws IOException
    {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new InputStreamReader(inputStream(path), "UTF-8"));

            String line;

            while ((line = reader.readLine()) != null)
            {
                result.append(line).append("\n");
            }
        }
        finally
        {
            if (reader != null)
            {
                reader.close();
            }
        }

        return result.toString();
    }

    public String[] files(String path) throws IOException
    {
        return context.getAssets().list(path);
    }

    public InputStream inputStream(String path) throws IOException
    {
        return context.getAssets().open(path);
    }

    public void fill(File file, Uri uri) throws IOException
    {
        ContentResolver contentResolver = context.getContentResolver();

        InputStream inputStream = contentResolver.openInputStream(uri);

        Files files = new Files();
        files.copy(inputStream, new FileOutputStream(file));
    }
}