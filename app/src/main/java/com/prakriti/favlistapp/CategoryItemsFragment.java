package com.prakriti.favlistapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.StringCharacterIterator;

public class CategoryItemsFragment extends Fragment {
// Detailed view of category items on Tablet device

    private RecyclerView rvItemsInCategory;
    Category category;

    private static final String CATEGORY_ARGS = "category_args";


    public CategoryItemsFragment() {
        // Required empty public constructor
    }

    public static CategoryItemsFragment newInstance(Category category) { // static
        // cannot access non static members
        CategoryItemsFragment categoryItemsFragment = new CategoryItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CATEGORY_ARGS, category); // category is passed to bundle of fragment
        categoryItemsFragment.setArguments(bundle);
        return categoryItemsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            // initialised in fragment if user is using Tablet / XLARGE screen

        if(getArguments() != null) { // if Bundle is not empty
            // fragments cannot use intents
            // fragments use arguments sent to it, which are received when fragment is instantiated
            // in this case, category obj will be sent to this fragment's Bundle whenever an instance of this fragment is created
            category = (Category) getArguments().getSerializable(CATEGORY_ARGS); // use a key exclusive to fragment class
            // cast coz Serializable obj returned is of type Category
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_items, container, false); // attach to root is done later
        if(view != null) {
            rvItemsInCategory = view.findViewById(R.id.rvItemsInCategory); // same recycler view as in activity_category_items.xml
            rvItemsInCategory.setAdapter(new ItemsRecyclerAdapter(category)); // for items
            rvItemsInCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        return view;
    }

    public void addItemToCategory(String item) {
        category.getItems().add(item);
        ItemsRecyclerAdapter itemsRecyclerAdapter = (ItemsRecyclerAdapter) rvItemsInCategory.getAdapter();
        itemsRecyclerAdapter.setCategory(category);
        itemsRecyclerAdapter.notifyDataSetChanged(); // updates recycler view

    }
}