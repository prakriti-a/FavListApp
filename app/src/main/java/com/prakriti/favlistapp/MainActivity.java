package com.prakriti.favlistapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CategoryFragment.OnCategoryInteractionListener {
// Fragment class implements other necessary interfaces, & communicates with activity

// SharedPreferences -> to save data quickly on light applications with low security
    // RecyclerView and CategoryManager are handled by the CategoryFragment class

    // MainActivity needs to reference CategoryFragment to access CategoryManager
    private CategoryFragment categoryFragment;
    // = CategoryFragment.newInstance(); // static method

    private boolean isTablet = false;
    private CategoryItemsFragment categoryItemsFragment;
    FloatingActionButton fab; // different functionalities if tablet
        // container for category items
    private FrameLayout categoryItemsFragmentContainer;

    public static final String CATEGORY_OBJECT_KEY = "CATEGORY_KEY";
    public static final int MAIN_ACTIVITY_REQUEST_CODE = 4113; // can be anything but must be unique

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            // called when activity is being launched
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialise UI Components Here

            // fragment in XML file
        categoryFragment = (CategoryFragment) getSupportFragmentManager().findFragmentById(R.id.category_fragment);
        categoryItemsFragmentContainer = findViewById(R.id.category_items_fragment_container);

        isTablet = categoryItemsFragmentContainer != null; // running on tab
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCreateCategoryDialog();
            }
        });

        // show/initialise fragment on activity on runtime
 //       getSupportFragmentManager().beginTransaction().add(R.id.category_fragment_container, categoryFragment).commit();
            // getSupportFragmentManager() -> allows adding/removing fragments at runtime

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//             //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                displayCreateCategoryDialog();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayCreateCategoryDialog() {
            // avoid hard coding string rsc to allow app language translations
        String alertTitle = getString(R.string.alert_title);
        String positiveButton = getString(R.string.pos_alert_btn);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // edit text for user to enter category
        EditText edtCategory = new EditText(this);
        edtCategory.setInputType(InputType.TYPE_CLASS_TEXT);

        alertDialog.setTitle(alertTitle);
        alertDialog.setView(edtCategory);
        alertDialog.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Category category = new Category(edtCategory.getText().toString(), new ArrayList<String>());
                    // after creating fragment we pass category to manager via fragment
            //    categoryManager.saveCategory(category); -> we don't use this anymore
                categoryFragment.giveCategoryToManager(category);

                    // get reference to adapter to update recycler view on button click
//                CategoryRecyclerAdapter categoryRecyclerAdapter = (CategoryRecyclerAdapter) categoryRecyclerView.getAdapter();
//                categoryRecyclerAdapter.addCategory(category);
                    // -> we don't use this after creating fragment, these lines are shifted to fragment class

                // to dismiss the dialog box
                dialog.dismiss();
                displayCategoryItems(category);
            }
        });
        alertDialog.create().show();
    }

    private void displayCategoryItems(Category category) {

        if (!isTablet) { // phone
            Intent categoryItemsIntent = new Intent(MainActivity.this, CategoryItemsActivity.class);
            // putExtra accepts Serializable type parameter, and Category implements Serializable
            categoryItemsIntent.putExtra(CATEGORY_OBJECT_KEY, category);

            // we want to retrieve and save the list of items entered in the category_items activity
            startActivityForResult(categoryItemsIntent, MAIN_ACTIVITY_REQUEST_CODE);
            // request code differentiates between multiple transitions from MainActivity to other activities
        }
        else {
                // remove old category items if already tapped, before replacing with new item list
            if(categoryItemsFragment != null) {
                // remove focus from item to categories
                getSupportFragmentManager().beginTransaction().remove(categoryItemsFragment).commit();
                categoryItemsFragment = null;
            }

            setTitle(category.getName());
            categoryItemsFragment = CategoryItemsFragment.newInstance(category);

            if(categoryItemsFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.category_items_fragment_container, categoryItemsFragment)
                        .addToBackStack(null).commit(); // items are visible
            }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayCreateCategoryItemDialog();
                }
            });

        }
    }

    private void displayCreateCategoryItemDialog() {
        final EditText itemEditText = new EditText(this);
        itemEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(this)
                .setTitle("Enter the Item Name:")
                .setView(itemEditText)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = itemEditText.getText().toString();
                        categoryItemsFragment.addItemToCategory(item);
                        dialog.dismiss();
                    }
                }).create().show();;
    }


        // this method is called when result is sent back from an activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MAIN_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if(data != null) {
//                categoryManager.saveCategory((Category) data.getSerializableExtra(CATEGORY_OBJECT_KEY));
//                updateCategories();
                categoryFragment.saveCategoryOnResult((Category) data.getSerializableExtra(CATEGORY_OBJECT_KEY));
                    // pass to fragment
            }
        }
    }

        // shifted to fragment class
//    private void updateCategories() {
//        ArrayList<Category> categories = categoryManager.retrieveCategories();
//        categoryRecyclerView.setAdapter(new CategoryRecyclerAdapter(categories, this)); // this -> listener
//    }

    @Override // from Fragment class
    public void categoryIsTapped(Category category) {
        displayCategoryItems(category);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setTitle(getString(R.string.app_name));
        if(categoryItemsFragment.category != null) {
            // use CategoryFragment to save data
            categoryFragment.getCategoryManager().saveCategory(categoryItemsFragment.category);
        }
        if(categoryItemsFragment != null) {
                // remove focus from item to categories
            getSupportFragmentManager().beginTransaction().remove(categoryItemsFragment).commit();
            categoryItemsFragment = null;
        }
            // fab should now be for new categories, not items
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCreateCategoryDialog();
            }
        });

    }
}