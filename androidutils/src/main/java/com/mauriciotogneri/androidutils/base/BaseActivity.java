package com.mauriciotogneri.androidutils.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;

import com.mauriciotogneri.androidutils.ActivityParameters;
import com.mauriciotogneri.androidutils.permissions.Permissions;
import com.mauriciotogneri.androidutils.permissions.PermissionsResult;

public abstract class BaseActivity<V extends BaseView, P extends BaseParameters> extends AppCompatActivity implements DialogDisplayer
{
    protected V view;
    protected P parameters;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        beforeCreate();

        loadParameters();

        View layout = setupView();

        ScreenTransition screenTransition = screenTransition();
        screenTransition.setupActivityEnter(this);

        if (disableScreenshot())
        {
            getWindow().setFlags(LayoutParams.FLAG_SECURE, LayoutParams.FLAG_SECURE);
        }

        afterCreate();

        initialize();
    }

    private void loadParameters()
    {
        parameters = parameters();

        if (parameters != null)
        {
            parameters.bind(this);
        }
    }

    private View setupView()
    {
        view = view();
        ViewGroup container = getWindow().getDecorView().findViewById(android.R.id.content);
        View layout = view.inflate(getLayoutInflater(), container);

        setContentView(layout);

        return layout;
    }

    protected ScreenTransition screenTransition()
    {
        return new ScreenTransition();
    }

    protected abstract V view();

    protected P parameters()
    {
        return null;
    }

    protected void beforeCreate()
    {
    }

    protected void afterCreate()
    {
    }

    protected void initialize()
    {
    }

    protected int fragmentContainer()
    {
        return 0;
    }

    protected boolean disableScreenshot()
    {
        return false;
    }

    protected Context context()
    {
        return this;
    }

    protected void addFragment(BaseFragment<?, ?> fragment)
    {
        if (fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            ScreenTransition screenTransition = fragment.screenTransition();
            screenTransition.setupFragment(transaction);

            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            transaction.replace(fragmentContainer(), fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    protected void removeFragment()
    {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void finish()
    {
        super.finish();

        ScreenTransition screenTransition = screenTransition();
        screenTransition.setupActivityExit(this);
    }

    protected Permissions permissions()
    {
        return new Permissions(this, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionsResult permissionsResult = new PermissionsResult(this);
        permissionsResult.process(requestCode, permissions, grantResults);
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

    @SuppressWarnings("unchecked")
    protected <A> A parameter(String key, A defaultValue)
    {
        ActivityParameters parameters = new ActivityParameters(getIntent());

        return parameters.parameter(key, defaultValue);
    }

    protected DialogDisplayer dialogDisplayer()
    {
        return this;
    }

    @Override
    public String string(int resId, Object... args)
    {
        return getString(resId, args);
    }

    @Override
    public FragmentManager fragmentManager()
    {
        return getSupportFragmentManager();
    }

    @Override
    @SuppressLint("MissingSuperCall")
    protected void onSaveInstanceState(Bundle outState)
    {
        // no call for super(), bug on API Level > 11
    }
}