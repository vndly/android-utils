package com.mauriciotogneri.androidutils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

import androidx.annotation.NonNull;

public class Localization
{
    private final Context context;

    public Localization(Context context)
    {
        this.context = context;
    }

    public Locale locale()
    {
        return getSystemLocale(configuration(resources()));
    }

    public boolean locale(Locale newLocale)
    {
        Resources resources = resources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration config = configuration(resources);

        Locale systemLocale = getSystemLocale(config);

        if (!systemLocale.equals(newLocale))
        {
            updateSystemLocale(config, newLocale);
            updateSystemConfiguration(resources, config, displayMetrics);

            return true;
        }
        else
        {
            return false;
        }
    }

    public void reset()
    {
        locale(Locale.getDefault());
    }

    // =============================================================================================

    private Resources resources()
    {
        return context.getResources();
    }

    private Configuration configuration(@NonNull Resources resources)
    {
        return resources.getConfiguration();
    }

    // =============================================================================================

    private Locale getSystemLocale(Configuration config)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return systemLocale(config);
        }
        else
        {
            return systemLocaleLegacy(config);
        }
    }

    private Locale systemLocaleLegacy(@NonNull Configuration config)
    {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Locale systemLocale(@NonNull Configuration config)
    {
        return config.getLocales().get(0);
    }

    // =============================================================================================

    private void updateSystemLocale(Configuration config, Locale locale)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            systemLocale(config, locale);
        }
        else
        {
            systemLocaleLegacy(config, locale);
        }
    }

    private void systemLocaleLegacy(@NonNull Configuration config, Locale locale)
    {
        config.locale = locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void systemLocale(@NonNull Configuration config, Locale locale)
    {
        config.setLocale(locale);
    }

    // =============================================================================================

    private void updateSystemConfiguration(@NonNull Resources resources, Configuration config, DisplayMetrics displayMetrics)
    {
        resources.updateConfiguration(config, displayMetrics);
    }
}