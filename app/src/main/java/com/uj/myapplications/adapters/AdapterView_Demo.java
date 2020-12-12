package com.uj.myapplications.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.uj.myapplications.R;
import com.uj.myapplications.fragments.AddMenuFragment;
import com.uj.myapplications.pojo.MenuPojo.Content;
import com.uj.myapplications.utility.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterView_Demo extends RecyclerView.Adapter<AdapterView_Demo.MyViewHolder> {
    String item;
    ArrayList<Content> arrayList = new ArrayList();
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMenuItem, tvQuantity;
        ImageView ivRemoveItem, ivRemoveQuantity, ivAddQuantity;

        public MyViewHolder(View view) {
            super(view);
            tvMenuItem = (TextView) view.findViewById(R.id.tvMenuItem);
            tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            ivRemoveItem = (ImageView) view.findViewById(R.id.ivRemoveItem);
            ivRemoveQuantity = (ImageView) view.findViewById(R.id.ivRemoveQuantity);
            ivAddQuantity = (ImageView) view.findViewById(R.id.ivAddQuantity);
        }
    }

    public AdapterView_Demo(Context context, ArrayList arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_custom_menu_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvMenuItem.setText(arrayList.get(position).getName());
        holder.ivRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMenuFragment.map.remove(arrayList.get(position));
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.ivAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(holder.tvQuantity.getText().toString());
                if (value <= 9) {
                    value = value + 1;
                    holder.tvQuantity.setText(value + "");
                    AddMenuFragment.map.put(arrayList.get(position).getName(), value);
                } else {
                    UtilityClass.showToast(context, "Max value reached");
                }
            }
        });
        holder.ivRemoveQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.tvQuantity.getText().toString().equals("1")) {
                    int value = Integer.parseInt(holder.tvQuantity.getText().toString());
                    value = value - 1;
                    holder.tvQuantity.setText(value + "");
                    AddMenuFragment.map.put(arrayList.get(position).getName(), value);
                } else {
                    UtilityClass.showToast(context, "Minimum cart value required");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}