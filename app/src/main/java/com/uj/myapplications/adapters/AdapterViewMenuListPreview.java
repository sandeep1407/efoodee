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

public class AdapterViewMenuListPreview extends RecyclerView.Adapter<AdapterViewMenuListPreview.MyViewHolder> {

    String item;
    ArrayList<Content> arrayList = new ArrayList();
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMenuItem, tvQuantity;

        public MyViewHolder(View view) {
            super(view);
            tvMenuItem = (TextView) view.findViewById(R.id.tvDishName);
            tvQuantity = (TextView) view.findViewById(R.id.tvDishQuantity);
        }
    }

    public AdapterViewMenuListPreview(Context context, ArrayList<Content> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_preview_meal_contents, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Content content = arrayList.get(position);
        holder.tvMenuItem.setText(content != null ? content.getName() : "N/A");
        // holder.tvQuantity.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}