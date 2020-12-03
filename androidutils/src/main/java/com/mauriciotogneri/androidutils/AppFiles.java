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
        String parent = context.getCacheDir().getParent();

        if (parent != null)
        {
            File appDir = new File(parent);

            if (appDir.exists())
            {
                File[] children = appDir.listFiles();

                if (children != null)
                {
                    for (File child : children)
                    {
                        if (!TextUtils.equals(child.getName(), "lib"))
                        {
                            delete(child);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void delete(File file)
    {
        if (file != null)
        {
            if (file.isDirectory())
            {
                File[] list = file.listFiles();

                if (list != null)
                {
                    for (File element : list)
                    {
                        delete(element);
                    }
                }
            }

            file.delete();
        }
    }
}