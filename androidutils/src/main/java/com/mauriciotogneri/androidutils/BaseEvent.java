package com.mauriciotogneri.androidutils;

import java.lang.reflect.Field;

public class BaseEvent
{
    @Override
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