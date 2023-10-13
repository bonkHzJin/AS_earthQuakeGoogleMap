package com.example.project2_hjin8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Inventory.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "inventory_table";
    private static final String COLUMN_ID = "_id";
    private static final String PRODUCT_NAME = "name";
    private static final String QUANTITY = "quantity";
    private static final String PRICE = "price";
    private static final String DESCRIPTION = "description";


    // constructor
    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // "CREATE TABLE my_db (_id INTEGER PRIMARY KEY AUTOINCREMENT, JOB_NAME TEXT)"
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRODUCT_NAME +  " TEXT, " +
                QUANTITY +      " INTEGER, " +
                PRICE +         " INTEGER, " +
                DESCRIPTION +   " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addJob(String name, String quantity, String price, String description){ // todo list only cares about the name of that job/task
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRODUCT_NAME, name); // column name and stuff you want to store in that column
        cv.put(QUANTITY, quantity);
        cv.put(PRICE, price);
        cv.put(DESCRIPTION, description);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "add_Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "add_Success", Toast.LENGTH_SHORT).show();
        }
    } // addJob() end

    Cursor readAllData(){ // return all data from database
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    } // readAllData() end

    void updateData(String row_id, String name, String quantity, String price, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT_NAME, name);
        cv.put(QUANTITY, quantity);
        cv.put(PRICE, price);
        cv.put(DESCRIPTION, description);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "update_failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "update_successful", Toast.LENGTH_SHORT).show();
        }
    } // updateData() end

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "delete_failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "delete_successful", Toast.LENGTH_SHORT).show();
        }
    }

}
