package com.mauriciotogneri.androidutils.secure;

import com.mauriciotogneri.androidutils.secure.AesCbcWithIntegrity.SecretKeys;

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