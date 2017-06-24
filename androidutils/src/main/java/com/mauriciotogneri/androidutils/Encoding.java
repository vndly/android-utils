package com.mauriciotogneri.androidutils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encoding
{
    private static final String UTF8 = "UTF-8";

    public static String toString(byte[] bytes) throws UnsupportedEncodingException
    {
        return new String(bytes, UTF8);
    }

    public static byte[] toByteArray(String text) throws UnsupportedEncodingException
    {
        return text.getBytes(UTF8);
    }

    public static String toBase64(byte[] bytes)
    {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static byte[] fromBase64(String text)
    {
        return Base64.decode(text, Base64.DEFAULT);
    }

    public static String urlEncode(String text) throws Exception
    {
        return URLEncoder.encode(text, UTF8);
    }

    public static String urlDecode(String text) throws Exception
    {
        return URLDecoder.decode(text, UTF8);
    }

    public static String md5(String input) throws NoSuchAlgorithmException
    {
        return hash("MD5", input);
    }

    public static String sha256(String input) throws NoSuchAlgorithmException
    {
        return hash("SHA-256", input);
    }

    public static String hash(String algorithm, String input) throws NoSuchAlgorithmException
    {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(input.getBytes());
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