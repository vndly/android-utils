package com.mauriciotogneri.androidutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

public class Intents
{
    private final Context context;

    public Intents(Context context)
    {
        this.context = context;
    }

    public boolean shareLink(String url)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.setType("text/url");

        return startActivity(intent);
    }

    public boolean sendEmail(String address, String subject, String text)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(String.format("mailto:%s", address)));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        return startActivity(intent);
    }

    public boolean callNumber(String phoneNumber)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(String.format("tel:%s", phoneNumber)));

        return startActivity(intent);
    }

    // TODO
    public boolean takePicture(Activity activity, int resultCode)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, path);

        return startActivityForResult(activity, intent, resultCode);
    }

    public boolean selectFromGallery(String type, Activity activity, int resultCode)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);

        return startActivityForResult(activity, intent, resultCode);
    }

    public boolean openFile(Uri uri, String type)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        return startActivity(intent);
    }

    public boolean shareFile(Uri uri, String type)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType(type);

        return startActivity(intent);
    }

    private boolean shareFiles(ArrayList<Uri> uris)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        return startActivity(intent);
    }

    public boolean openAppPlayStore(String packageName)
    {
        String uri = String.format("market://details?id=%s", packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

        return startActivity(intent);
    }

    public void openAppPlayStoreOnWeb(String packageName)
    {
        String uri = String.format("http://play.google.com/store/apps/details?id=%s", packageName);

        openWebpage(uri, android.R.color.white);
    }

    public void openWebpage(String url, @ColorRes int color)
    {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.addDefaultShareMenuItem();
        builder.setToolbarColor(ContextCompat.getColor(context, color));

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }

    public boolean startActivity(Intent intent)
    {
        if (intent.resolveActivity(context.getPackageManager()) != null)
        {
            context.startActivity(intent);

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean startActivityForResult(Activity activity, Intent intent, int requestCode)
    {
        if (intent.resolveActivity(activity.getPackageManager()) != null)
        {
            activity.startActivityForResult(intent, requestCode);

            return true;
        }
        else
        {
            return false;
        }
    }
}