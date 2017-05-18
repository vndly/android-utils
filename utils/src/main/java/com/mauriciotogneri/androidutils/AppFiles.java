package com.mauriciotogneri.androidutils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

public class AppFiles
{
    private final Context context;

    public AppFiles(Context context)
    {
        this.context = context;
    }

    public void clean()
    {
        File appDir = new File(context.getCacheDir().getParent());

        if (appDir.exists())
        {
            File[] children = appDir.listFiles();

            for (File folder : children)
            {
                if (!TextUtils.equals(folder.getName(), "lib"))
                {
                    deleteFolder(folder);
                }
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteFolder(File folder)
    {
        if (folder != null)
        {
            if (folder.isDirectory())
            {
                File[] list = folder.listFiles();

                for (File element : list)
                {
                    deleteFolder(element);
                }
            }

            folder.delete();
        }
    }
}