package com.mauriciotogneri.androidutils.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mauriciotogneri.androidutils.Keyboard;
import com.mauriciotogneri.androidutils.Keyboard.KeyboardObserver;
import com.mauriciotogneri.androidutils.uibinder.UiBinder;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.ViewBinding;

public abstract class BaseView<O, C>
{
    private View view;
    private Context context;
    private final int viewId;
    private final ViewBinding viewBinding;
    protected final C ui;
    protected final O observer;

    protected BaseView(@LayoutRes int viewId, O observer, C viewContainer)
    {
        this.viewId = viewId;
        this.observer = observer;
        this.ui = viewContainer;
        this.viewBinding = null;
    }

    @SuppressWarnings("unchecked")
    protected BaseView(O observer, ViewBinding viewBinding)
    {
        this.viewId = 0;
        this.observer = observer;
        this.ui = (C) viewBinding;
        this.viewBinding = viewBinding;
    }

    public View inflate(LayoutInflater inflater, ViewGroup container)
    {
        context = inflater.getContext();

        if (viewId != 0)
        {
            view = inflateOld(inflater, container);
        }
        else if (viewBinding != null)
        {
            view = inflateNew();
        }

        initialize();

        return view;
    }

    private View inflateOld(LayoutInflater inflater, ViewGroup container)
    {
        View view = inflater.inflate(viewId, container, false);

        if (container == null)
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(params);
        }

        UiBinder uiBinder = new UiBinder();
        uiBinder.bind(view, this);

        if (ui != null)
        {
            uiBinder.bind(view, ui);
        }

        return view;
    }

    private View inflateNew()
    {
        return viewBinding.getRoot();
    }

    protected void initialize()
    {
    }

    protected String string(@StringRes int resId)
    {
        return context().getString(resId);
    }

    protected String string(@StringRes int resId, Object... formatArgs)
    {
        return context().getString(resId, formatArgs);
    }

    protected int color(@ColorRes int resId)
    {
        return ContextCompat.getColor(context(), resId);
    }

    protected View inflate(@LayoutRes int resource, ViewGroup root, boolean attachToRoot)
    {
        return LayoutInflater.from(context()).inflate(resource, root, attachToRoot);
    }

    protected View inflate(@LayoutRes int resource)
    {
        return inflate(resource, null, false);
    }

    protected void visible(View view)
    {
        view.setVisibility(View.VISIBLE);
    }

    protected void invisible(View view)
    {
        view.setVisibility(View.INVISIBLE);
    }

    protected void gone(View view)
    {
        view.setVisibility(View.GONE);
    }

    protected void background(View view, @ColorRes int color)
    {
        view.setBackgroundColor(color(color));
    }

    protected void post(Runnable runnable)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }

    protected void post(Runnable runnable, long delay)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, delay);
    }

    protected void showKeyboard()
    {
        Keyboard keyboard = new Keyboard(context());
        keyboard.show(view);
    }

    protected void hideKeyboard()
    {
        Keyboard keyboard = new Keyboard(context());
        keyboard.hide(view);
    }

    protected void onKeyboardChange(View root, int threshold, KeyboardObserver observer)
    {
        Keyboard keyboard = new Keyboard(context());
        keyboard.onChange(root, threshold, observer);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T view(String name)
    {
        int resId = context().getResources().getIdentifier(name, "id", context().getPackageName());

        return (T) findViewById(resId);
    }

    public View findViewById(@IdRes int id)
    {
        return view.findViewById(id);
    }

    protected Context context()
    {
        return context;
    }

    public View view()
    {
        return view;
    }
}