package com.mauriciotogneri.androidutils.secure;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mauriciotogneri.androidutils.Encoding;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EncryptedPreferences implements SharedPreferences
{
    private final SecureStorage secureStorage;
    private final SecureStorageKey secureStorageKey;
    private final SharedPreferences sharedPreferences;

    public EncryptedPreferences(Context context, String name, boolean holdKey) throws Exception
    {
        this.secureStorage = new SecureStorage(context);

        if (holdKey)
        {
            this.secureStorageKey = secureStorage.key();
        }
        else
        {
            this.secureStorageKey = null;
        }

        if ((name == null) || name.isEmpty())
        {
            this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        else
        {
            this.sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
    }

    public EncryptedPreferences(Context context, boolean holdKey) throws Exception
    {
        this(context, null, holdKey);
    }

    private SecureStorageKey key() throws Exception
    {
        return (secureStorageKey != null) ? secureStorageKey : secureStorage.key();
    }

    @Override
    public Map<String, Object> getAll()
    {
        Map<String, Object> result = new HashMap<>();

        for (Entry<String, ?> entry : sharedPreferences.getAll().entrySet())
        {
            result.put(entry.getKey(), decrypt(entry.getValue().toString()));
        }

        return result;
    }

    @Override
    public Editor edit()
    {
        return new Editor()
        {
            private final Editor editor = sharedPreferences.edit();

            @Override
            public Editor putString(String key, String value)
            {
                return editor.putString(key, encrypt(value));
            }

            @Override
            public Editor putStringSet(String key, Set<String> value)
            {
                try
                {
                    StringBuilder set = new StringBuilder();

                    for (String element : value)
                    {
                        if (set.length() != 0)
                        {
                            set.append(",");
                        }

                        set.append(Encoding.urlEncode(element));
                    }

                    return editor.putString(key, encrypt(set.toString()));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Editor putInt(String key, int value)
            {
                return editor.putString(key, encrypt(value));
            }

            @Override
            public Editor putLong(String key, long value)
            {
                return editor.putString(key, encrypt(value));
            }

            @Override
            public Editor putFloat(String key, float value)
            {
                return editor.putString(key, encrypt(value));
            }

            @Override
            public Editor putBoolean(String key, boolean value)
            {
                return editor.putString(key, encrypt(value));
            }

            private <T> String encrypt(T value)
            {
                try
                {
                    return secureStorage.encrypt(String.valueOf(value), key());
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Editor remove(String key)
            {
                return editor.remove(key);
            }

            @Override
            public Editor clear()
            {
                return editor.clear();
            }

            @Override
            public boolean commit()
            {
                return editor.commit();
            }

            @Override
            public void apply()
            {
                editor.apply();
            }
        };
    }

    @Override
    public String getString(String key, String defaultValue)
    {
        if (sharedPreferences.contains(key))
        {
            return decrypt(key);
        }
        else
        {
            return defaultValue;
        }
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defaultValue)
    {
        if (sharedPreferences.contains(key))
        {
            try
            {
                Set<String> set = new HashSet<>();

                String[] elements = decrypt(key).split(",");

                for (String element : elements)
                {
                    set.add(Encoding.urlDecode(element));
                }

                return set;
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            return defaultValue;
        }
    }

    @Override
    public int getInt(String key, int defaultValue)
    {
        if (sharedPreferences.contains(key))
        {
            return Integer.parseInt(decrypt(key));
        }
        else
        {
            return defaultValue;
        }
    }

    @Override
    public long getLong(String key, long defaultValue)
    {
        if (sharedPreferences.contains(key))
        {
            return Long.parseLong(decrypt(key));
        }
        else
        {
            return defaultValue;
        }
    }

    @Override
    public float getFloat(String key, float defaultValue)
    {
        if (sharedPreferences.contains(key))
        {
            return Float.parseFloat(decrypt(key));
        }
        else
        {
            return defaultValue;
        }
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue)
    {
        if (sharedPreferences.contains(key))
        {
            return Boolean.parseBoolean(decrypt(key));
        }
        else
        {
            return defaultValue;
        }
    }

    private String decrypt(String key)
    {
        try
        {
            return secureStorage.decrypt(sharedPreferences.getString(key, null), key());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean contains(String key)
    {
        return sharedPreferences.contains(key);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener)
    {
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener)
    {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }
}