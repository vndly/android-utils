package com.mauriciotogneri.androidutils.uibinder;

import java.lang.reflect.Method;

class CallableMethod
{
    private final Method method;
    private final Object target;

    public CallableMethod(Method method, Object target)
    {
        this.method = method;
        this.target = target;
    }

    public void call(Object... parameters)
    {
        try
        {
            method.invoke(target, parameters);
        }
        catch (Exception e)
        {
            throw new RuntimeException(String.format("Error invoking method '%s'", method.getName()), e.getCause());
        }
    }
}