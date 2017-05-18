package com.mauriciotogneri.androidutils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// TODO: sha1, sha256, etc
public class Encoding
{
    private static final String UTF8 = "UTF-8";

    public String toString(byte[] bytes) throws UnsupportedEncodingException
    {
        return new String(bytes, UTF8);
    }

    public byte[] toByteArray(String text) throws UnsupportedEncodingException
    {
        return text.getBytes(UTF8);
    }

    public String toBase64(byte[] bytes)
    {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public byte[] fromBase64(String text)
    {
        return Base64.decode(text, Base64.DEFAULT);
    }

    public String md5(String text) throws NoSuchAlgorithmException
    {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(text.getBytes());
        byte messageDigest[] = digest.digest();

        StringBuilder hexString = new StringBuilder();

        for (byte element : messageDigest)
        {
            String hex = Integer.toHexString(0xFF & element);

            while (hex.length() < 2)
            {
                hex = "0" + hex;
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }
}