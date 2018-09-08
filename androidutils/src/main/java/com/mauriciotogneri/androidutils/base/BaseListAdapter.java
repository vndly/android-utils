package com.mauriciotogneri.androidutils.base;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class BaseListAdapter<T, V extends BaseListViewHolder> extends ArrayAdapter<T>
{
    protected final LayoutInflater inflater;
    private final int resourceId;
    private final List<T> list;

    public BaseListAdapter(Context context, int resourceId, List<T> list)
    {
        super(context, android.R.layout.simple_list_item_1, list);

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

    protected int color(@ColorRes int colorId)
    {
        return ContextCompat.getColor(getContext(), colorId);
    }
}