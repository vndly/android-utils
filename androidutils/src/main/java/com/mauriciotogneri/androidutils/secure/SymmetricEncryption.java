package com.mauriciotogneri.androidutils.secure;

import com.mauriciotogneri.androidutils.secure.AesCbcWithIntegrity.CipherTextIvMac;
import com.mauriciotogneri.androidutils.secure.AesCbcWithIntegrity.SecretKeys;
import com.mauriciotogneri.javautils.Encoding;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

class SymmetricEncryption
{
    SymmetricEncryption()
    {
    }

    SecretKeys keys(byte[] confidentialityKey, byte[] integrityKey)
    {
        return new SecretKeys(secretKey(confidentialityKey), secretKey(integrityKey));
    }

    private SecretKey secretKey(byte[] bytes)
    {
        return new SecretKeySpec(bytes, 0, bytes.length, "AES");
    }

    SecretKeys generateKeys() throws Exception
    {
        return AesCbcWithIntegrity.generateKey();
    }

    byte[] encrypt(byte[] data, SecretKeys keys) throws Exception
    {
        CipherTextIvMac encrypted = AesCbcWithIntegrity.encrypt(data, keys);

        return Encoding.toByteArray(encrypted.toString());
    }

    byte[] decrypt(byte[] data, SecretKeys keys) throws Exception
    {
        CipherTextIvMac cipherTextIvMac = new CipherTextIvMac(Encoding.toString(data));

        return AesCbcWithIntegrity.decrypt(cipherTextIvMac, keys);
    }
}