package com.mauriciotogneri.androidutils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class Dump
{
    private final File source;
    private final File target;

    public Dump(Context context, File target)
    {
        this.source = context.getFilesDir().getParentFile();
        this.target = target;
    }

    public void start() throws Exception
    {
        processFolder(source);
    }

    private void processFolder(File file) throws Exception
    {
        for (File element : file.listFiles())
        {
            if (element.isDirectory())
            {
                processFolder(element);
            }
            else
            {
                processFile(element);
            }
        }
    }

    private void processFile(File file) throws Exception
    {
        File destination = new File(target.getAbsolutePath(), file.getAbsolutePath().replace(source.getAbsolutePath(), ""));
        File parent = destination.getParentFile();

        if (parent.exists() || destination.getParentFile().mkdirs())
        {
            copy(file, destination);
        }
        else
        {
            throw new Exception(String.format("Couldn't copy '%s' to '%s'", file.getAbsolutePath(), destination.getAbsolutePath()));
        }
    }

    private void copy(File a, File b) throws Exception
    {
        FileChannel src = new FileInputStream(a).getChannel();
        FileChannel dest = new FileOutputStream(b).getChannel();
        dest.transferFrom(src, 0, src.size());
    }
}