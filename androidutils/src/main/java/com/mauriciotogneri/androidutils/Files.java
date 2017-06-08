package com.mauriciotogneri.androidutils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Files
{
    public File file(File file) throws IOException
    {
        File parent = file.getParentFile();

        boolean parentValid = parent.exists() || parent.mkdirs();
        boolean fileValid = file.exists() || file.createNewFile();

        if (parentValid && fileValid)
        {
            return file;
        }
        else
        {
            throw new IOException(String.format("File '%s' cannot be created", file.getAbsolutePath()));
        }
    }

    public File file(String path) throws IOException
    {
        return file(new File(path));
    }

    public void copy(InputStream inputStream, OutputStream outputStream) throws IOException
    {
        int read;
        byte[] bytes = new byte[1024];

        while ((read = inputStream.read(bytes)) != -1)
        {
            outputStream.write(bytes, 0, read);
        }

        outputStream.flush();

        close(outputStream);
    }

    public void close(Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch (Exception e)
            {
                // ignore
            }
        }
    }
}