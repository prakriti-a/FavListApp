package com.prakriti.favlistapp;

import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private TextView txtCategoryNumber, txtCategoryName;

    public CategoryViewHolder(View view) {
        super(view);
        txtCategoryName = view.findViewById(R.id.txtCategoryName);
        txtCategoryNumber = view.findViewById(R.id.txtCategoryNumber);
    }

    public TextView getTxtCategoryNumber() {
        return txtCategoryNumber;
    }

    public TextView getTxtCategoryName() {
        return txtCategoryName;
    }
}
