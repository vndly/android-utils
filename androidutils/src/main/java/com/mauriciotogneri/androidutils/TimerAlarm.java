package com.mauriciotogneri.androidutils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

public class TimerAlarm
{
    private final long timeout;
    private long lastTimestamp = 0;
    private final Runnable task;
    private final AlarmManager alarmManager;
    private final PendingIntent pendingIntent;

    public TimerAlarm(Context context, Runnable task, Intent intent, int timeout, TimeUnit unit)
    {
        this.timeout = unit.toMillis(timeout);
        this.task = task;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public synchronized void start()
    {
        touch();
        reset();
    }

    public synchronized void touch()
    {
        lastTimestamp = System.currentTimeMillis();
    }

    public synchronized void stop()
    {
        alarmManager.cancel(pendingIntent);
    }

    public synchronized void check()
    {
        long timeDifference = System.currentTimeMillis() - lastTimestamp;

        if (timeDifference >= timeout)
        {
            stop();
            task.run();
        }
        else
        {
            reset();
        }
    }

    private synchronized void reset()
    {
        stop();

        long newTimeout = timeout - (System.currentTimeMillis() - lastTimestamp);

        if (newTimeout > 0)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + newTimeout, pendingIntent);
            }
            else
            {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + newTimeout, pendingIntent);
            }
        }
    }
}