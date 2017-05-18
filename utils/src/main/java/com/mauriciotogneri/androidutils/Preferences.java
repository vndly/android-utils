package com.mauriciotogneri.androidutils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Base64;

import java.util.Map;
import java.util.Set;

public class Preferences
{
    private final SharedPreferences preferences;

    protected Preferences(SharedPreferences preferences)
    {
        this.preferences = preferences;
    }

    // =============================================================================================

    public Map<String, ?> getAll()
    {
        return preferences.getAll();
    }

    public boolean contains(String key)
    {
        return preferences.contains(key);
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener)
    {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener)
    {
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    // =============================================================================================

    public void clear()
    {
        preferences.edit().clear().commit();
    }

    public void remove(String key)
    {
        preferences.edit().remove(key).commit();
    }

    // =============================================================================================

    public void save(String key, String value)
    {
        preferences.edit().putString(key, value).commit();
    }

    public void save(String key, Set<String> value)
    {
        preferences.edit().putStringSet(key, value).commit();
    }

    public void save(String key, int value)
    {
        preferences.edit().putInt(key, value).commit();
    }

    public void save(String key, long value)
    {
        preferences.edit().putLong(key, value).commit();
    }

    public void save(String key, float value)
    {
        preferences.edit().putFloat(key, value).commit();
    }

    public void save(String key, boolean value)
    {
        preferences.edit().putBoolean(key, value).commit();
    }

    public void save(String key, byte[] value)
    {
        save(key, Base64.encodeToString(value, Base64.DEFAULT));
    }

    // =============================================================================================

    public String load(String key, String defaultValue)
    {
        return preferences.getString(key, defaultValue);
    }

    public Set<String> load(String key, Set<String> defaultValue)
    {
        return preferences.getStringSet(key, defaultValue);
    }

    public int load(String key, int defaultValue)
    {
        return preferences.getInt(key, defaultValue);
    }

    public long load(String key, long defaultValue)
    {
        return preferences.getLong(key, defaultValue);
    }

    public float load(String key, float defaultValue)
    {
        return preferences.getFloat(key, defaultValue);
    }

    public boolean load(String key, boolean defaultValue)
    {
        return preferences.getBoolean(key, defaultValue);
    }

    public byte[] load(String key, byte[] defaultValue)
    {
        return contains(key) ? Base64.decode(load(key, ""), Base64.DEFAULT) : defaultValue;
    }
}