package com.prakriti.favlistapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView txtItem;

    public ItemViewHolder(View itemView) {
        super(itemView);
        txtItem = itemView.findViewById(R.id.txtItem);
    }

    public TextView getTxtItem() {
        return txtItem;
    }
}
