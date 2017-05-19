package com.mauriciotogneri.androidutils;

import org.greenrobot.eventbus.EventBus;

public class Bus
{
    private Bus()
    {
    }

    public static void register(Object subscriber)
    {
        try
        {
            if (!EventBus.getDefault().isRegistered(subscriber))
            {
                EventBus.getDefault().register(subscriber);
            }
        }
        catch (Exception e)
        {
            // ignore
        }
    }

    public static void unregister(Object subscriber)
    {
        try
        {
            if (EventBus.getDefault().isRegistered(subscriber))
            {
                EventBus.getDefault().unregister(subscriber);
            }
        }
        catch (Exception e)
        {
            // ignore
        }
    }

    public static void post(Object event)
    {
        EventBus.getDefault().post(event);
    }
}