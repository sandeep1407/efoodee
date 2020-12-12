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
import com.uj.myapplications.pojo.MenuPojo.Extra;

import java.util.ArrayList;

public class AdapterMenuExtraItems extends RecyclerView.Adapter<AdapterMenuExtraItems.MyViewHolder> {

    String item;
    Context context;
    ArrayList<Extra> list = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMenuItem, tvPriceOfItem;
        ImageView ivRemoveItem;

        public MyViewHolder(View view) {
            super(view);
            tvMenuItem = (TextView) view.findViewById(R.id.tvItemExtra);
            tvPriceOfItem = (TextView) view.findViewById(R.id.tvRupeesExtra);
            ivRemoveItem = (ImageView) view.findViewById(R.id.ivCancelItem);
        }
    }

    public AdapterMenuExtraItems(Context context, ArrayList<Extra> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_menu_items_extra, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvMenuItem.setText(list.get(position).getName());
        holder.tvPriceOfItem.setText(list.get(position).getPrice().toString());
        holder.ivRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMenuFragment.mapExtraItems.remove(list.get(position));
                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}