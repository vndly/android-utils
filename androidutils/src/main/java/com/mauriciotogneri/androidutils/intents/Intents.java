package com.mauriciotogneri.androidutils.intents;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.mauriciotogneri.javautils.Encoding;

import java.util.ArrayList;

public class Intents
{
    public static IntentOperation custom(Intent intent)
    {
        return new IntentOperation(intent);
    }

    public static IntentOperation custom(Intent intent, int requestCode)
    {
        return new IntentOperation(intent, requestCode);
    }

    public static IntentOperation shareLink(String url)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.setType("text/url");

        return new IntentOperation(intent);
    }

    public static IntentOperation sendEmail(String address, String subject, String text)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(String.format("mailto:%s", address)));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        return new IntentOperation(intent);
    }

    public static IntentOperation callNumber(String phoneNumber)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(String.format("tel:%s", phoneNumber)));

        return new IntentOperation(intent);
    }

    public static IntentOperation takePicture(Context context, Uri uri)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        else
        {
            ClipData clip = ClipData.newUri(context.getContentResolver(), "picture", uri);

            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        return new IntentOperation(intent);
    }

    public static IntentOperation pictureThumbnail()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        return new IntentOperation(intent);
    }

    public static IntentOperation selectFromGallery(String type)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);

        return new IntentOperation(intent);
    }

    @TargetApi(VERSION_CODES.KITKAT)
    public static IntentOperation selectFromGallery(String[] types)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, types);

        return new IntentOperation(intent);
    }

    public static IntentOperation openFile(Uri uri, String type)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        return new IntentOperation(intent);
    }

    public static IntentOperation shareFile(Uri uri, String type)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType(type);

        return new IntentOperation(intent);
    }

    private static IntentOperation shareFiles(ArrayList<Uri> uris)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        return new IntentOperation(intent);
    }

    public static IntentOperation address(String address) throws Exception
    {
        Uri uri = Uri.parse(String.format("geo:0,0?q=%s", Encoding.urlEncode(address)));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return new IntentOperation(intent);
    }

    public static IntentOperation route(double latitude, double longitude)
    {
        Uri uri = Uri.parse(String.format("geo:%s,%s?q=%s,%s", latitude, longitude, latitude, longitude));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return new IntentOperation(intent);
    }

    public static IntentOperation openAppPlayStore(String packageName)
    {
        String uri = String.format("market://details?id=%s", packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

        return new IntentOperation(intent);
    }

    public static void openAppPlayStoreOnWeb(Context context, String packageName)
    {
        String uri = String.format("http://play.google.com/store/apps/details?id=%s", packageName);

        openWebpage(context, uri, android.R.color.white);
    }

    public static void openWebpage(Context context, String url, @ColorRes int color)
    {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.addDefaultShareMenuItem();
        builder.setToolbarColor(ContextCompat.getColor(context, color));

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}