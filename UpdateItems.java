package com.example.project2_hjin8;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateItems extends AppCompatActivity {
    /**
     * only the quantity, price, and description should be editable.
     * name and ID should not be modified by the user
     */

    TextView name;
    EditText desc, price, quantity;
    Button btn_update;
    String itm_id, itm_name, itm_price, itm_quantity, itm_description;

    // five view items need to be modified with hint and etc
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Updating Inventory");

        // binding objects
        name = findViewById(R.id.tv_item_name);
        desc = findViewById(R.id.Et_changeDesc);
        price = findViewById(R.id.Et_price);
        quantity = findViewById(R.id.Et_quantity);
        btn_update = findViewById(R.id.btn_update);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (desc.getText().toString().trim().length() != 0) itm_description = desc.getText().toString().trim();
                if (price.getText().toString().trim().length() != 0) itm_price = price.getText().toString().trim();
                if (quantity.getText().toString().trim().length() != 0) itm_quantity = quantity.getText().toString().trim();

                MyDatabaseHelper my_db = new MyDatabaseHelper(UpdateItems.this);
                my_db.updateData(itm_id,itm_name,itm_quantity,itm_price,itm_description);
            }
        });

        getIntentFromUpdate();


    }
    void getIntentFromUpdate() {
            if (getIntent().hasExtra("id")
                    && getIntent().hasExtra("name")
                    && getIntent().hasExtra("description")
                    && getIntent().hasExtra("price")
                    && getIntent().hasExtra("quantity")) {
                // getting data
                itm_name = getIntent().getStringExtra("name");
                itm_description = getIntent().getStringExtra("description");
                itm_price = getIntent().getStringExtra("price");
                itm_quantity = getIntent().getStringExtra("quantity");
                itm_id = getIntent().getStringExtra("id");

                // setting intent data
                desc.setHint(itm_description);
                name.setText(itm_name);
                price.setText(itm_price);
                quantity.setHint(itm_quantity);

            } else {
                Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
            }
    }



}
