package com.mauriciotogneri.androidutils;

import org.greenrobot.eventbus.EventBus;

public class Bus
{
    private Bus()
    {
    }

    public static void register(Object subscriber)
    {
        if (!EventBus.getDefault().isRegistered(subscriber))
        {
            EventBus.getDefault().register(subscriber);
        }
    }

    public static void unregister(Object subscriber)
    {
        if (EventBus.getDefault().isRegistered(subscriber))
        {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    public static void post(Object event)
    {
        EventBus.getDefault().post(event);
    }
}