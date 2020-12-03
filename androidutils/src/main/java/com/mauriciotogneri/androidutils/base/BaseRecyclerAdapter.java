package com.mauriciotogneri.androidutils.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mauriciotogneri.androidutils.base.BaseRecyclerViewHolder.OnViewHolderClicked;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseRecyclerAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> implements OnViewHolderClicked
{
    private final int resourceId;
    private final List<T> items;
    private final Context context;
    private final LayoutInflater inflater;
    private final OnItemSelected<T> onItemSelected;

    public BaseRecyclerAdapter(Context context, int resourceId, List<T> items, OnItemSelected<T> onItemSelected)
    {
        this.resourceId = resourceId;
        this.items = items;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.onItemSelected = onItemSelected;
    }

    public BaseRecyclerAdapter(Context context, int resourceId, List<T> items)
    {
        this(context, resourceId, items, null);
    }

    public BaseRecyclerAdapter(Context context, int resourceId)
    {
        this(context, resourceId, new ArrayList<>(), null);
    }

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View rowView = inflater.inflate(rowViewResource(viewType), parent, false);

        return viewHolder(rowView);
    }

    public void onBindViewHolder(@NonNull V holder, int position)
    {
        T item = item(position);

        fillView(holder, item, position, holder.itemView);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    protected abstract V viewHolder(View view);

    protected abstract void fillView(V viewHolder, T item, int position, View rowView);

    protected int rowViewResource(int viewType)
    {
        return resourceId;
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
        return ContextCompat.getColor(context(), colorId);
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

    public T item(int position)
    {
        return items.get(position);
    }

    public void update()
    {
        notifyDataSetChanged();
    }

    public void clear()
    {
        items.clear();
    }

    public void update(List<T> list)
    {
        items.clear();
        items.addAll(list);

        update();
    }

    public void add(T element)
    {
        items.add(element);

        update();
    }

    public void addAll(List<T> list)
    {
        items.addAll(list);

        update();
    }

    public void set(List<T> list)
    {
        clear();
        addAll(list);
    }

    public List<T> items()
    {
        return items;
    }

    @Override
    public void onViewHolderClick(int position)
    {
        onItemSelected.onItemClick(item(position), position);
    }

    @Override
    public void onViewHolderLongClick(int position)
    {
        onItemSelected.onItemLongClick(item(position), position);
    }

    public interface OnItemSelected<T>
    {
        void onItemClick(T item, int position);

        void onItemLongClick(T item, int position);
    }

    public static class CustomDividerItemDecoration extends DividerItemDecoration
    {
        public CustomDividerItemDecoration(Context context, int orientation, @DrawableRes int resId)
        {
            super(context, orientation);

            Drawable drawable = ContextCompat.getDrawable(context, resId);

            if (drawable != null)
            {
                setDrawable(drawable);
            }
        }
    }
}