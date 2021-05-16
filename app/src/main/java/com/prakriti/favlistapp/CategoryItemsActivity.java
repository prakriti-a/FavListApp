package com.prakriti.favlistapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryItemsActivity extends AppCompatActivity {
// activity used to display items for Phone device, fragment used for Tablet device

    private RecyclerView rvItemsInCategory;
    FloatingActionButton fabAddItems;

    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);

           // Category_object_key is a static variable
            // cast to Serializable to Category object
        category = (Category) getIntent().getSerializableExtra(MainActivity.CATEGORY_OBJECT_KEY);

          // to set title on Action Bar
        setTitle(category.getName()); // initialize vars before using them

            // initialize UI items
        rvItemsInCategory = findViewById(R.id.rvItemsInCategory);
        rvItemsInCategory.setAdapter(new ItemsRecyclerAdapter(category));
        rvItemsInCategory.setLayoutManager(new LinearLayoutManager(this));

        fabAddItems = findViewById(R.id.fabAddItems);

        fabAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we need adapter to add items to recycler view and viewholder for how to display them
                displayItemCreationDialog();
            }
        });
    }

    private void displayItemCreationDialog() {
        final EditText edtItemName = new EditText(this); // final bcoz it is used inside an anonymous inner class
        edtItemName.setInputType(InputType.TYPE_CLASS_TEXT);

            // create alert dialog object and chain the function calls
        new AlertDialog.Builder(this)
                .setTitle(R.string.item_alert_title)
                .setView(edtItemName)
                .setPositiveButton(R.string.item_pos_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String itemName = edtItemName.getText().toString();
                        category.getItems().add(itemName);
                        ItemsRecyclerAdapter itemsRecyclerAdapter = (ItemsRecyclerAdapter) rvItemsInCategory.getAdapter();
                        itemsRecyclerAdapter.notifyItemInserted(category.getItems().size() - 1); // last index
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
            // the entered data will be sent back to MainActivity and MainActivity will run the logic on saving the item list
    }

      // method for controlling transition between activities
    @Override
    public void onBackPressed() {

        Bundle bundle = new Bundle();
            // add category data
        bundle.putSerializable(MainActivity.CATEGORY_OBJECT_KEY, category); // same key to return object
        Intent intent = new Intent();
        intent.putExtras(bundle);
            // to inform that it is okay to retrieve data
        setResult(Activity.RESULT_OK, intent);

        super.onBackPressed(); // don't remove this

    }
}