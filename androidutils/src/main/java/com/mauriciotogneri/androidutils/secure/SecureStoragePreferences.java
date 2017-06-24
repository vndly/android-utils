package com.mauriciotogneri.androidutils.secure;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mauriciotogneri.androidutils.Encoding;

class SecureStoragePreferences
{
    private static final String FIELD_KEY_CONFIDENTIALITY = "key.confidentiality";
    private static final String FIELD_KEY_INTEGRITY = "key.integrity";

    private final SharedPreferences sharedPreferences;

    SecureStoragePreferences(Context context)
    {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // =============================================================================================

    void clear()
    {
        sharedPreferences.edit().clear().commit();
    }

    private boolean contains(String key)
    {
        return sharedPreferences.contains(key);
    }

    private void save(String key, byte[] value)
    {
        sharedPreferences.edit().putString(key, Encoding.toBase64(value)).commit();
    }

    private byte[] get(String key)
    {
        return Encoding.fromBase64(sharedPreferences.getString(key, null));
    }

    // =============================================================================================

    void confidentialityKey(byte[] key)
    {
        save(FIELD_KEY_CONFIDENTIALITY, key);
    }

    void integrityKey(byte[] key)
    {
        save(FIELD_KEY_INTEGRITY, key);
    }

    byte[] confidentialityKey()
    {
        return get(FIELD_KEY_CONFIDENTIALITY);
    }

    byte[] integrityKey()
    {
        return get(FIELD_KEY_INTEGRITY);
    }

    boolean hasKeys()
    {
        return contains(FIELD_KEY_CONFIDENTIALITY) && contains(FIELD_KEY_INTEGRITY);
    }
}