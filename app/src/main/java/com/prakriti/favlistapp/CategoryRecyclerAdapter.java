package com.prakriti.favlistapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
// it is the Adapter's job on how to display the items in RecyclerView, and to respond if any items are clicked

    // interface for handling Category clicks
    interface CategoryIsClicked {

        void categoryIsClicked(Category category);

    }
    private CategoryIsClicked categoryIsClickedInterface; // reference to object that implements it

    //  String[] categories = {"Hobbies", "Sports", "Games", "Electronics", "Food", "Countries"};
        // user creates categories
    private ArrayList<Category> categories;

    public CategoryRecyclerAdapter(ArrayList<Category> categories, CategoryIsClicked categoryIsClickedInterface) {
        this.categories = categories;

            // this refers to an object that implements the interface, not the interface itself as interfaces cannot be initialised
        this.categoryIsClickedInterface = categoryIsClickedInterface;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.category_viewholder, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // this method is called for each view/item added to recycler view
        holder.getTxtCategoryNumber().setText(Integer.toString(position + 1));
        holder.getTxtCategoryName().setText(categories.get(position).getName());

            // holder is of type CategoryViewHolder, so it has access to each itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // can now call the method inside CategoryIsClicked interface
                    // pass the Category from the arraylist by specifying position
                categoryIsClickedInterface.categoryIsClicked(categories.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addCategory(Category category) {
        categories.add(category);
        notifyItemInserted(categories.size() - 1);
    }
}
