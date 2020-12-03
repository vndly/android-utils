package com.mauriciotogneri.androidutils.base;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;

public class BaseEvent
{
    @Override
    @NonNull
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for (Field field : getClass().getFields())
        {
            try
            {
                if (builder.length() > 0)
                {
                    builder.append(", ");
                }

                builder.append(field.getName());
                builder.append("=");
                builder.append(field.get(this));
            }
            catch (Exception e)
            {
                // ignore
            }
        }

        return String.format(":[%s]", builder.toString());
    }
}