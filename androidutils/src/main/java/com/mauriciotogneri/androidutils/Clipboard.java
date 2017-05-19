package com.mauriciotogneri.androidutils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class Clipboard
{
    private final Context context;

    public Clipboard(Context context)
    {
        this.context = context;
    }

    public void copy(String text)
    {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", text);
        clipboard.setPrimaryClip(clip);
    }
}