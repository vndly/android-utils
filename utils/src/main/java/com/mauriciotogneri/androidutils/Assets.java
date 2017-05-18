package com.mauriciotogneri.androidutils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Assets
{
    private final Context context;

    public Assets(Context context) throws IOException
    {
        this.context = context;
    }

    public String read(String path) throws IOException
    {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(path), "UTF-8"));

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
}