package com.mauriciotogneri.androidutils;

import android.content.Context;

import com.mauriciotogneri.javautils.Record;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Installation
{
    private final Context context;

    private static final String FILE_NAME = "installation";

    public Installation(Context context)
    {
        this.context = context;
    }

    public String id() throws IOException
    {
        File installation = new File(context.getFilesDir(), Installation.FILE_NAME);

        if (!installation.exists())
        {
            write(installation, UUID.randomUUID().toString());
        }

        return read(installation);
    }

    private String read(File file) throws IOException
    {
        Record record = new Record(file);

        return record.string();
    }

    private void write(File file, String id) throws IOException
    {
        Record record = new Record(file);

        record.write(id);
    }

    public boolean remove()
    {
        File installation = new File(context.getFilesDir(), Installation.FILE_NAME);

        return installation.exists() && installation.delete();
    }
}