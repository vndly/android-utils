package com.mauriciotogneri.androidutils.uibinder.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import androidx.annotation.IdRes;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
public @interface BindView
{
    @IdRes int value();
}