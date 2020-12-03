package com.mauriciotogneri.androidutils.logger;

import android.text.TextUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

import androidx.annotation.NonNull;

public class FileLogger
{
    private final BufferedWriter bufferedWriter;

    public FileLogger(File parent, String pattern)
    {
        this.bufferedWriter = bufferedWriter(parent, pattern);
    }

    public synchronized void write(String message)
    {
        if (!TextUtils.isEmpty(message))
        {
            try
            {
                bufferedWriter.write(message);
                bufferedWriter.flush();
            }
            catch (Exception e)
            {
                // ignore
            }
        }
    }

    @NonNull
    private BufferedWriter bufferedWriter(File parent, @NonNull String pattern)
    {
        try
        {
            if (parent.exists() || parent.mkdirs())
            {
                DateTimeFormatter fileNameFormat = DateTimeFormat.forPattern(pattern).withLocale(Locale.ENGLISH);
                File file = new File(parent, String.format("%s.log", fileNameFormat.print(DateTime.now())));

                if (file.exists() || file.createNewFile())
                {
                    return new BufferedWriter(new FileWriter(file, true));
                }
                else
                {
                    throw new RuntimeException(String.format("Cannot create file: %s", file.getAbsolutePath()));
                }
            }
            else
            {
                throw new RuntimeException(String.format("Cannot create parent folder: %s", parent.getAbsolutePath()));
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}