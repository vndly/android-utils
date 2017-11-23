package com.mauriciotogneri.androidutils.secure;

import android.content.Context;

import com.mauriciotogneri.javautils.Encoding;
import com.tozny.crypto.android.AesCbcWithIntegrity.SecretKeys;

import java.security.KeyPair;

import javax.crypto.SecretKey;

public class SecureStorage
{
    private final SecureStoragePreferences preferences;
    private final SymmetricEncryption symmetricEncryption;
    private final AsymmetricEncryption asymmetricEncryption;

    public SecureStorage(Context context)
    {
        this.preferences = new SecureStoragePreferences(context);
        this.symmetricEncryption = new SymmetricEncryption();
        this.asymmetricEncryption = new AsymmetricEncryption(context);
    }

    public synchronized String encrypt(String data) throws Exception
    {
        return encrypt(data, key());
    }

    public synchronized String decrypt(String data) throws Exception
    {
        return decrypt(data, key());
    }

    public synchronized byte[] encrypt(byte[] data) throws Exception
    {
        return encrypt(data, key());
    }

    public synchronized byte[] decrypt(byte[] data) throws Exception
    {
        return decrypt(data, key());
    }

    public synchronized String encrypt(String data, SecureStorageKey key) throws Exception
    {
        byte[] encryptedData = encrypt(Encoding.toByteArray(data), key);

        return Encoding.toString(encryptedData);
    }

    public synchronized String decrypt(String data, SecureStorageKey key) throws Exception
    {
        byte[] decryptedData = decrypt(Encoding.toByteArray(data), key);

        return Encoding.toString(decryptedData);
    }

    public synchronized byte[] encrypt(byte[] data, SecureStorageKey key) throws Exception
    {
        return symmetricEncryption.encrypt(data, key.keys());
    }

    public synchronized byte[] decrypt(byte[] data, SecureStorageKey key) throws Exception
    {
        return symmetricEncryption.decrypt(data, key.keys());
    }

    public synchronized SecureStorageKey key() throws Exception
    {
        KeyPair keyPair = asymmetricEncryption.keys();

        if (preferences.hasKeys())
        {
            byte[] confidentialityKey = preferences.confidentialityKey();
            byte[] integrityKey = preferences.integrityKey();

            byte[] confidentialityKeyDecrypted = asymmetricEncryption.decrypt(confidentialityKey, keyPair);
            byte[] integrityKeyDecrypted = asymmetricEncryption.decrypt(integrityKey, keyPair);

            return new SecureStorageKey(symmetricEncryption.keys(confidentialityKeyDecrypted, integrityKeyDecrypted));
        }
        else
        {
            SecretKeys secretKeys = symmetricEncryption.generateKeys();

            SecretKey confidentialityKey = secretKeys.getConfidentialityKey();
            byte[] confidentialityKeyEncrypted = asymmetricEncryption.encrypt(confidentialityKey.getEncoded(), keyPair);
            preferences.confidentialityKey(confidentialityKeyEncrypted);

            SecretKey integrityKey = secretKeys.getIntegrityKey();
            byte[] integrityKeyEncrypted = asymmetricEncryption.encrypt(integrityKey.getEncoded(), keyPair);
            preferences.integrityKey(integrityKeyEncrypted);

            return new SecureStorageKey(secretKeys);
        }
    }

    public synchronized boolean isAvailable()
    {
        try
        {
            KeyPair keyPair = asymmetricEncryption.keys();

            return (keyPair != null);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public synchronized void reset() throws Exception
    {
        asymmetricEncryption.removeKeystore();
        preferences.clear();
    }
}