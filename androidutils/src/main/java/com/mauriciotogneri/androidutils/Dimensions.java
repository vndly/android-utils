package com.mauriciotogneri.androidutils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Dimensions
{
    private final Context context;

    public Dimensions(Context context)
    {
        this.context = context;
    }

    public int dpToPixel(int dp)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        return (int) (dp * (metrics.densityDpi / 160f));
    }

    public int pixelsToDp(int px)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        return (int) (px / (metrics.densityDpi / 160f));
    }
}