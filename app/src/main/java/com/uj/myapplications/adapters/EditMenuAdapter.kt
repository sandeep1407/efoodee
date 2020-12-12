package com.uj.myapplications.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uj.myapplications.R
import kotlinx.android.synthetic.main.listitem_edit_menu_current.view.*

class EditMenuAdapter(val items: ArrayList<String>, val context: Context) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_edit_menu_current, p0, false))
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.tvAnimalType.text = items[p1]
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvAnimalType = view.txt_menu_name
}