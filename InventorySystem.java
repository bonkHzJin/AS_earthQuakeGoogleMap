package com.example.project2_hjin8;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class InventorySystem extends AppCompatActivity {

    MyDatabaseHelper inventory_db;
    ArrayList<String> id_list, name_list, quantity_list, price_list, description_list;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_system);

        // rename
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Inventory System");

        // recyclerView
        recyclerView = findViewById(R.id.recyclerView1);

        // Database
        inventory_db = new MyDatabaseHelper(InventorySystem.this);

        // ArrayList
        id_list = new ArrayList<>();
        name_list = new ArrayList<>();
        quantity_list = new ArrayList<>();
        price_list = new ArrayList<>();
        description_list = new ArrayList<>();

        Cursor c = inventory_db.readAllData();
        if (c.getCount() == 0 ) {
            // this means there's no data in the db
            // generate at least 3 for testing
            inventory_db.addJob("Apple", "100", "1.05", "An apple a day, keeps the doctor away.");
            inventory_db.addJob("Banana", "50", "1.20", "I can't think of anything regarding bananas");
            inventory_db.addJob("Lemon", "120", "1.40", "When life gives you lemons, chuck em' right back and aim for the stomach. -- internet");
        }

        displayData();

        customAdapter = new CustomAdapter(InventorySystem.this,this, id_list, name_list, quantity_list, price_list, description_list);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(InventorySystem.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    void displayData(){
        Cursor cursor = inventory_db.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id_list.add(cursor.getString(0));
                name_list.add(cursor.getString(1));
                quantity_list.add(cursor.getString(2));
                price_list.add(cursor.getString(3));
                description_list.add(cursor.getString(4));
            }
        }
    }
}