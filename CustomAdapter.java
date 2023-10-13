package com.example.project2_hjin8;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    ArrayList id_list, name_list, quantity_list, price_list, description_list;
    Activity activity;

    CustomAdapter(Context context,
                  Activity activity,
                  ArrayList id_list,
                  ArrayList name_list,
                  ArrayList quantity_list,
                  ArrayList price_list,
                  ArrayList description_list) {
        this.activity = activity;
        this.context = context;
        this.id_list = id_list;
        this.name_list = name_list;
        this.quantity_list = quantity_list;
        this.price_list = price_list;
        this.description_list = description_list;
        System.out.println("id_list : " + id_list.size() + "\n" +
                "name_list : " + name_list.size() + "\n" +
                "quantity_list : " + quantity_list.size() + "\n" +
                "price_list : " + price_list.size() + "\n" +
                "description_list : " + description_list.size());

    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_newrow, parent, false);
        return new MyViewHolder(view);
    }


    /**
     * get all the values from the array and bind it to the textview
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder,  int position) {
        holder.id_txt.setText(String.valueOf(id_list.get(holder.getAdapterPosition())));
        holder.name_txt.setText(String.valueOf(name_list.get(holder.getAdapterPosition())));
        holder.quantity_txt.setText("Quantity : " + String.valueOf(quantity_list.get(holder.getAdapterPosition())));
        holder.price_txt.setText("price : $" + String.valueOf(price_list.get(holder.getAdapterPosition())));

        // set onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(name_list.get(holder.getAdapterPosition()));
                String desc = String.valueOf(description_list.get(holder.getAdapterPosition()));
                Snackbar snackbar = Snackbar
                        .make(view, name + ": " + desc, Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        });

        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // I'd like to send over ID, name, price, quantity and description
                Intent intent = new Intent(context, UpdateItems.class);
                intent.putExtra("id", String.valueOf(id_list.get(holder.getAdapterPosition())));
                intent.putExtra("name", String.valueOf(name_list.get(holder.getAdapterPosition())));
                intent.putExtra("price", String.valueOf(price_list.get(holder.getAdapterPosition())));
                intent.putExtra("quantity", String.valueOf(quantity_list.get(holder.getAdapterPosition())));
                intent.putExtra("description", String.valueOf(description_list.get(holder.getAdapterPosition())));
                activity.startActivityForResult(intent, 1);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return id_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id_txt, name_txt, quantity_txt, price_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_txt = itemView.findViewById(R.id.tv_id);
            name_txt = itemView.findViewById(R.id.tv_name);
            quantity_txt = itemView.findViewById(R.id.tv_quantity);
            price_txt = itemView.findViewById(R.id.tv_price);

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }


    }
}
