package com.mauriciotogneri.androidutils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Screen
{
    private final Context context;

    public Screen(Context context)
    {
        this.context = context;
    }

    public ScreenSize size()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        return new ScreenSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static class ScreenSize
    {
        private final int width;
        private final int height;

        public ScreenSize(int width, int height)
        {
            this.width = width;
            this.height = height;
        }

        public int width()
        {
            return width;
        }

        public int height()
        {
            return height;
        }
    }
}