package com.mauriciotogneri.androidutils;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

public class Intents
{
    private final IntentTarget target;

    public Intents(IntentTarget target)
    {
        this.target = target;
    }

    public Intents(final Activity activity)
    {
        this.target = new IntentTarget()
        {
            @Override
            public Context context()
            {
                return activity;
            }

            @Override
            public void startActivity(Intent intent)
            {
                target.startActivity(intent);
            }

            @Override
            public void startActivityForResult(Intent intent, int requestCode)
            {
                target.startActivityForResult(intent, requestCode);
            }
        };
    }

    public Intents(final Fragment fragment)
    {
        this.target = new IntentTarget()
        {
            @Override
            public Context context()
            {
                return fragment.getContext();
            }

            @Override
            public void startActivity(Intent intent)
            {
                fragment.startActivity(intent);
            }

            @Override
            public void startActivityForResult(Intent intent, int requestCode)
            {
                fragment.startActivityForResult(intent, requestCode);
            }
        };
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

    public boolean takePicture(Uri uri, int resultCode)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        else
        {
            ClipData clip = ClipData.newUri(target.context().getContentResolver(), "picture", uri);

            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        return startActivityForResult(target, intent, resultCode);
    }

    public boolean pictureThumbnail(int resultCode)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        return startActivityForResult(target, intent, resultCode);
    }

    public boolean selectFromGallery(String type, int resultCode)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);

        return startActivityForResult(target, intent, resultCode);
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

    public boolean route(String address)
    {
        try
        {
            Uri uri = Uri.parse(String.format("geo:0,0?q=%s", Encoding.urlEncode(address)));

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            return startActivity(intent);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean route(double latitude, double longitude)
    {
        try
        {
            Uri uri = Uri.parse(String.format("geo:%s,%s?q=%s,%s", latitude, longitude, latitude, longitude));

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            return startActivity(intent);
        }
        catch (Exception e)
        {
            return false;
        }
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
        builder.setToolbarColor(ContextCompat.getColor(target.context(), color));

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(target.context(), Uri.parse(url));
    }

    public boolean startActivity(Intent intent)
    {
        if (intent.resolveActivity(target.context().getPackageManager()) != null)
        {
            target.startActivity(intent);

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean startActivityForResult(IntentTarget target, Intent intent, int requestCode)
    {
        if (intent.resolveActivity(target.context().getPackageManager()) != null)
        {
            target.startActivityForResult(intent, requestCode);

            return true;
        }
        else
        {
            return false;
        }
    }

    public interface IntentTarget
    {
        Context context();

        void startActivity(Intent intent);

        void startActivityForResult(Intent intent, int requestCode);
    }
}