package com.prakriti.favlistapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

    // Interface Design Pattern
public class CategoryFragment extends Fragment implements CategoryRecyclerAdapter.CategoryIsClicked {
// Fragment needs to talk to MainActivity
    // Fragment is embedded inside activity, and is instantiated after the activity is instantiated
    // Only activity can call another activity, fragment cannot


    private RecyclerView categoryRecyclerView;
    private CategoryManager categoryManager;
    // variables can be initialised only after fragment has a context (in onAttach method), not in the class outside a method

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }


    interface OnCategoryInteractionListener { // interface connects two classes
        void categoryIsTapped(Category category);
    }
    private OnCategoryInteractionListener listenerObject;


    public CategoryFragment() {
        // Required empty public constructor
    }

    // the fragment will be made responsible to show the recycler view
    @Override
    public void onAttach(@NonNull Context context) {
        // context - activity in which fragment is embedded
        // lifecycle method for fragment, gives the context for a fragment
        // onAttach is called before onCreate (before fragment is created)
        super.onAttach(context);

        if(context instanceof OnCategoryInteractionListener) // if context implements the interface
        {   // bcoz the context/activity implements the interface for sure
            listenerObject = (OnCategoryInteractionListener) context;
            categoryManager  = new CategoryManager(context);
        }
        else
            throw new RuntimeException("Activity must implement OnCategoryInteractionListener");
    }


        // factory method to create a new instance of this fragment & return it
    public static CategoryFragment newInstance() {
        // called when other activities want to create a new fragment instance
        return new CategoryFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // called when fragment is being created
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // called when fragment's activity is created and fragment's view hierarchy is instantiated
        // can be used to initialise UI components once the activity's UI is ready
        // called when onCreate() inside MainActivity is finished executing

        ArrayList<Category> categories = categoryManager.retrieveCategories();

        if (getView() != null) {
            // getView() gets the root view of fragment layout
            categoryRecyclerView = getView().findViewById(R.id.rvCategory);
            categoryRecyclerView.setAdapter(new CategoryRecyclerAdapter(categories, this));
            // since MainActivity implements the interface
            categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // pass activity of fragment as context
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, lifecycle method of fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        // called when fragment is no longer attached to activity, or when activity is destroyed
        // called after onDestroy, lifecycle method of fragment

        listenerObject = null; // reference not req anymore
    }


    @Override
    public void categoryIsClicked(Category category) {
        listenerObject.categoryIsTapped(category);
    }


    public void giveCategoryToManager(Category category) {
        categoryManager.saveCategory(category); // pass to CategoryManager

        // get reference to adapter to update recycler view on button click
        CategoryRecyclerAdapter categoryRecyclerAdapter = (CategoryRecyclerAdapter) categoryRecyclerView.getAdapter();
        categoryRecyclerAdapter.addCategory(category);
    }

    public void saveCategoryOnResult(Category category) {
        // use CategoryManager to save the data
        categoryManager.saveCategory(category);
        updateRecyclerView(); // update recycler view with added category
    }

    private void updateRecyclerView() {
        ArrayList<Category> categories = categoryManager.retrieveCategories();
        categoryRecyclerView.setAdapter(new CategoryRecyclerAdapter(categories, this)); // this -> listener
    }

}