package com.mauriciotogneri.androidutils.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.mauriciotogneri.androidutils.uibinder.UiBinder;

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder implements OnClickListener, OnLongClickListener
{
    private final OnViewHolderClicked onViewHolderClicked;

    protected BaseRecyclerViewHolder(View view, OnViewHolderClicked onViewHolderClicked)
    {
        super(view);

        if (onViewHolderClicked != null)
        {
            this.onViewHolderClicked = onViewHolderClicked;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }
        else
        {
            this.onViewHolderClicked = null;
        }

        UiBinder uiBinder = new UiBinder();
        uiBinder.bind(view, this);
    }

    @Override
    public void onClick(View view)
    {
        if (onViewHolderClicked != null)
        {
            onViewHolderClicked.onViewHolderClick(getLayoutPosition());
        }
    }

    @Override
    public boolean onLongClick(View v)
    {
        if (onViewHolderClicked != null)
        {
            onViewHolderClicked.onViewHolderLongClick(getLayoutPosition());

            return true;
        }
        else
        {
            return false;
        }
    }

    public interface OnViewHolderClicked
    {
        void onViewHolderClick(int position);

        void onViewHolderLongClick(int position);
    }
}