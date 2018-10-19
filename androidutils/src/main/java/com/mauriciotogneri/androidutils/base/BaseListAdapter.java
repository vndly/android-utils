package com.mauriciotogneri.androidutils.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class BaseListAdapter<T, V extends BaseListViewHolder> extends ArrayAdapter<T>
{
    private final Context context;
    private final LayoutInflater inflater;
    private final int resourceId;
    private final List<T> list;

    public BaseListAdapter(Context context, int resourceId, List<T> list)
    {
        super(context, android.R.layout.simple_list_item_1, list);

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
        this.list = list;
    }

    public void set(List<T> list)
    {
        clear();
        add(list);
    }

    public void add(List<T> list)
    {
        addAll(list);
        notifyDataSetChanged();
    }

    public List<T> list()
    {
        return list;
    }

    protected abstract void fillView(V viewHolder, T item, int position, View rowView);

    protected abstract V viewHolder(View view);

    @Override
    @NonNull
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        View rowView = convertView;

        if (rowView == null)
        {
            rowView = generatedRowView(position, null, parent);
        }
        else
        {
            updatedRowView(position, convertView, parent);
        }

        V viewHolder = (V) rowView.getTag();

        T item = getItem(position);

        fillView(viewHolder, item, position, rowView);

        return rowView;
    }

    protected int rowViewResource(int position)
    {
        return resourceId;
    }

    protected View generatedRowView(int position, View convertView, ViewGroup parent)
    {
        View rowView = inflater.inflate(rowViewResource(position), parent, false);
        V viewHolder = viewHolder(rowView);
        rowView.setTag(viewHolder);

        return rowView;
    }

    protected void updatedRowView(int position, View convertView, ViewGroup parent)
    {
    }

    protected View inflate(@LayoutRes int resource, ViewGroup root, boolean attachToRoot)
    {
        return inflater.inflate(resource, root, attachToRoot);
    }

    protected View inflate(@LayoutRes int resource)
    {
        return inflate(resource, null, false);
    }

    protected int color(@ColorRes int colorId)
    {
        return ContextCompat.getColor(getContext(), colorId);
    }

    protected String string(@StringRes int resId)
    {
        return context.getString(resId);
    }

    protected String string(@StringRes int resId, Object... formatArgs)
    {
        return context.getString(resId, formatArgs);
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

    protected Context context()
    {
        return context;
    }
}