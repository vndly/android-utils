package com.mauriciotogneri.androidutils.secure;

import com.tozny.crypto.android.AesCbcWithIntegrity.SecretKeys;

public class SecureStorageKey
{
    private final SecretKeys keys;

    SecureStorageKey(SecretKeys keys)
    {
        this.keys = keys;
    }

    SecretKeys keys()
    {
        return keys;
    }
}