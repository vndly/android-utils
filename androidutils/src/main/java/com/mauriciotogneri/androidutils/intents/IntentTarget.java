package com.mauriciotogneri.androidutils.intents;

import android.content.Context;
import android.content.Intent;

public interface IntentTarget
{
    Context context();

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);
}