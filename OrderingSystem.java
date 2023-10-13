package com.example.project2_hjin8;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class OrderingSystem extends AppCompatActivity {

    private Spinner spinner;
    private static ArrayList id_list, name_list, quantity_list, price_list, description_list;
    private static ArrayList<Integer> orderedItem;
    private static ArrayList<Integer> orderedAmount;
    private static MyDatabaseHelper myDB;
    Button btn_add, btn_remove, btn_finish;
    private static int what; // which item got selected, it's the position of arraylist/spinner.
    EditText Et_orderQuantity;
    String summary;
    TextView tv_summary, tv_final;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering_system);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Order product");

        myDB = new MyDatabaseHelper(OrderingSystem.this);

        // initialize stuff
        summary = "";
        what = -1;
        spinner = findViewById(R.id.spinner);
        id_list = new ArrayList();
        name_list = new ArrayList();
        quantity_list = new ArrayList();
        price_list = new ArrayList();
        description_list = new ArrayList();
        btn_add = findViewById(R.id.btn_add);
        btn_remove = findViewById(R.id.btn_remove);
        btn_finish = findViewById(R.id.btn_finish);
        Et_orderQuantity = findViewById(R.id.Et_OrderAmount);
        tv_summary = findViewById(R.id.Tv_summary);
        tv_final = findViewById(R.id.Tv_total);

        orderedItem = new ArrayList<>();
        orderedAmount = new ArrayList<>();

        // read all data from DB
        Cursor c = myDB.readAllData();
        if (c.getCount() == 0) {
            myDB.addJob("Apple", "100", "1.05", "An apple a day, keeps the doctor away.");
            myDB.addJob("Banana", "50", "1.20", "I can't think of anything regarding bananas");
            myDB.addJob("Lemon", "120", "1.40", "When life gives you lemons, chuck em' right back and aim for the stomach. -- internet");
        }

        c = myDB.readAllData();
        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                id_list.add(c.getString(0));
                name_list.add(c.getString(1));
                quantity_list.add(c.getString(2));
                price_list.add(c.getString(3));
                description_list.add(c.getString(4));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, name_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // set on click/select listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                getItemInfo(position);
                what = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // no amount of item
                // too much item
                // else
                if (Et_orderQuantity.getText().toString().length() != 0) {
                    int amount = Integer.parseInt(Et_orderQuantity.getText().toString().trim());
                    int stockAmount = Integer.parseInt(quantity_list.get(what).toString());
                    if (amount < 0) {
                        noAmout();
                    } else if (amount > stockAmount) {
                        moreThanStockAmout();
                    } else {
                        if (!orderedItem.contains(what)) {
                            orderedItem.add(what);
                            orderedAmount.add(Integer.parseInt(Et_orderQuantity.getText().toString().trim()));
                        } else {
                            for (int i = 0; i < orderedItem.size(); i++) {
                                if (orderedItem.get(i).equals(what)) {
                                    if (orderedAmount.get(i) + amount > stockAmount) {
                                        moreThanStockAmout();
                                    } else {
                                        orderedAmount.set(i, amount + orderedAmount.get(i));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    noAmout();
                }
                Et_orderQuantity.setText("");
                updateShoppingCart();
                tv_final.setText("_");
            }
        });

        // Instruction:  make it difficult for user to delete stuff by accident
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = new AlertDialog.Builder(OrderingSystem.this)
                        .setTitle("Attention")
                        .setMessage("delete all of selected items from the current order?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // current order does contain such item.
                                if (orderedItem.contains(what)) {
                                    for (int j = 0; j < orderedItem.size(); j++) {
                                        if (orderedItem.get(j).equals(what)) {
                                            orderedItem.remove(j);
                                            orderedAmount.remove(j);
                                            updateShoppingCart();
                                            break;
                                        }
                                    }
                                } else {  // current order does not contain such item;
                                    deleteNull();
                                }
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                alert.show();
                Et_orderQuantity.setText("");
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String total_msg = "TOTAL COST: $";
                double total_cost = 0;
                for (int i = 0; i < orderedItem.size(); i++) {
                    total_cost += (orderedAmount.get(i) * Double.parseDouble(price_list.get(orderedItem.get(i)).toString()));
                }
                String total_cost_string = String.format("%,.2f", total_cost);
                total_msg += total_cost_string;
                tv_final.setText(total_msg);
                String temp = "";
                for (int i = 0; i < orderedItem.size(); i++) {
//                    System.out.println(orderedItem.get(i)  + ", " + orderedAmount.get(i) + "\n");
                    int pos = orderedItem.get(i);
                    int cnt = orderedAmount.get(i);
                    String new_quantity = (Integer.parseInt(quantity_list.get(pos).toString()) - cnt) + "";
                    myDB.updateData(
                            id_list.get(pos).toString(),
                            name_list.get(pos).toString(),
                            new_quantity,
                            price_list.get(pos).toString(),
                            description_list.get(pos).toString());
                }
                reset();
            }
        });
    }

    private void getItemInfo(int position) {
        String item = spinner.getSelectedItem().toString();
        Toast.makeText(this, item + ", " + quantity_list.get(position) +
                " in stock.", Toast.LENGTH_SHORT).show();
    }

    private void updateShoppingCart() {
        String output = "";
        for (int i = 0; i < orderedItem.size(); i++) {
            double tmp = (orderedAmount.get(i) * Double.parseDouble(price_list.get(orderedItem.get(i)).toString()));
            String amount = String.format("%,.2f", tmp);

            output += name_list.get(orderedItem.get(i)) + "(" + orderedAmount.get(i) + ") $" + amount + "\n";
        }
        tv_summary.setText(output);
    }


    private void reset(){
        id_list.clear();
        price_list.clear();
        name_list.clear();
        description_list.clear();
        quantity_list.clear();
        orderedItem.clear();
        orderedAmount.clear();

//        updateShoppingCart();
//        tv_summary.setText("_");
//        tv_final.setText("_");
//        Et_orderQuantity.setText("");

        Cursor c = myDB.readAllData();
        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                id_list.add(c.getString(0));
                name_list.add(c.getString(1));
                quantity_list.add(c.getString(2));
                price_list.add(c.getString(3));
                description_list.add(c.getString(4));
            }
        }
    }

    private void noAmout(){
        Toast.makeText(this, "you can't order 0 of this.", Toast.LENGTH_SHORT).show();
    }

    private void moreThanStockAmout(){
        Toast.makeText(this, "we don't have that many in stock.", Toast.LENGTH_SHORT).show();
    }

    private void deleteNull(){
        Toast.makeText(this, "you have not ordered this.", Toast.LENGTH_SHORT).show();
    }




}