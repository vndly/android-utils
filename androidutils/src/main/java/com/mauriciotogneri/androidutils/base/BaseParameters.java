package com.mauriciotogneri.androidutils.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class BaseParameters
{
    protected void bind(BaseFragment<?, ?, ?> fragment)
    {
        bind(new ParameterHolder(fragment));
    }

    protected void bind(BaseActivity<?, ?, ?> activity)
    {
        bind(new ParameterHolder(activity));
    }

    private void bind(ParameterHolder holder)
    {
        Field[] fields = getClass().getDeclaredFields();

        for (Field field : fields)
        {
            Parameter annotation = field.getAnnotation(Parameter.class);

            if (annotation != null)
            {
                try
                {
                    Class<?> type = field.getType();

                    if (type.equals(int.class))
                    {
                        int value = holder.parameter(annotation.value(), annotation.defaultInt());
                        field.setInt(this, value);
                    }
                    else if (type.equals(boolean.class))
                    {
                        boolean value = holder.parameter(annotation.value(), annotation.defaultBoolean());
                        field.setBoolean(this, value);
                    }
                    else if (type.equals(String.class))
                    {
                        String value = holder.parameter(annotation.value(), annotation.defaultString());
                        field.set(this, value);
                    }
                    else
                    {
                        Object value = holder.parameter(annotation.value(), null);
                        field.set(this, value);
                    }
                }
                catch (Exception e)
                {
                    // ignore
                }
            }
        }
    }

    public static class ParameterHolder
    {
        private BaseFragment<?, ?, ?> fragment = null;
        private BaseActivity<?, ?, ?> activity = null;

        public ParameterHolder(BaseFragment<?, ?, ?> fragment)
        {
            this.fragment = fragment;
        }

        public ParameterHolder(BaseActivity<?, ?, ?> activity)
        {
            this.activity = activity;
        }

        protected <T> T parameter(String key, T defaultValue)
        {
            if (fragment != null)
            {
                return fragment.parameter(key, defaultValue);
            }
            else if (activity != null)
            {
                return activity.parameter(key, defaultValue);
            }
            else
            {
                return null;
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Parameter
    {
        String value();

        int defaultInt() default 0;

        boolean defaultBoolean() default false;

        String defaultString() default "";
    }
}