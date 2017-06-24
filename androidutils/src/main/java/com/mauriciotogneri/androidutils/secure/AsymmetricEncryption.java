package com.mauriciotogneri.androidutils.secure;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

class AsymmetricEncryption
{
    private final Context context;

    private static final String KEYSTORE_ALIAS = "AppKeystore";
    private static final String CYPHER_TYPE = "RSA/ECB/PKCS1Padding";
    private static final String ANDROID_KEY_STORE_NAME = "AndroidKeyStore";

    AsymmetricEncryption(Context context)
    {
        this.context = context;
    }

    KeyPair keys() throws Exception
    {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME);
        keyStore.load(null);

        if (keyStore.containsAlias(KEYSTORE_ALIAS))
        {
            Key key = keyStore.getKey(KEYSTORE_ALIAS, null);
            Certificate certificate = keyStore.getCertificate(KEYSTORE_ALIAS);
            PublicKey publicKey = certificate.getPublicKey();

            return new KeyPair(publicKey, (PrivateKey) key);
        }
        else
        {
            return generateKeys();
        }
    }

    private KeyPair generateKeys() throws Exception
    {
        AlgorithmParameterSpec spec;

        if (Build.VERSION.SDK_INT >= VERSION_CODES.M)
        {
            spec = keyGenSpec23();
        }
        else
        {
            spec = keyGenSpec18();
        }

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", ANDROID_KEY_STORE_NAME);
        keyPairGenerator.initialize(spec);

        return keyPairGenerator.generateKeyPair();
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR2)
    @SuppressWarnings("deprecation")
    private AlgorithmParameterSpec keyGenSpec18()
    {
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        end.add(Calendar.YEAR, 100);

        KeyPairGeneratorSpec.Builder builder = new KeyPairGeneratorSpec.Builder(context);
        builder.setAlias(KEYSTORE_ALIAS);
        builder.setStartDate(start.getTime());
        builder.setEndDate(end.getTime());
        builder.setSerialNumber(BigInteger.ONE);
        builder.setSubject(new X500Principal("CN=" + KEYSTORE_ALIAS));

        return builder.build();
    }

    @TargetApi(VERSION_CODES.M)
    private AlgorithmParameterSpec keyGenSpec23()
    {
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        end.add(Calendar.YEAR, 100);

        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(KEYSTORE_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
        builder.setKeyValidityStart(start.getTime());
        builder.setKeyValidityEnd(end.getTime());
        builder.setKeySize(2048);
        builder.setCertificateSerialNumber(BigInteger.ONE);
        builder.setCertificateSubject(new X500Principal("CN=" + KEYSTORE_ALIAS));
        builder.setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512);
        builder.setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1);
        builder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1);

        return builder.build();
    }

    void removeKeystore() throws Exception
    {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME);
        keyStore.load(null);

        if (keyStore.containsAlias(KEYSTORE_ALIAS))
        {
            keyStore.deleteEntry(KEYSTORE_ALIAS);
        }
    }

    byte[] encrypt(byte[] data, KeyPair keys) throws Exception
    {
        Cipher cipher = Cipher.getInstance(CYPHER_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, keys.getPublic());

        return cipher.doFinal(data);
    }

    byte[] decrypt(byte[] data, KeyPair keys) throws Exception
    {
        Cipher cipher = Cipher.getInstance(CYPHER_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, keys.getPrivate());

        return cipher.doFinal(data);
    }
}