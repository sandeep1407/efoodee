package com.uj.myapplications.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uj.myapplications.R
import com.uj.myapplications.pojo.MenuPojo.Extra
import kotlinx.android.synthetic.main.listitem_checkbox.view.*

class CheckBoxAdapter(val items: HashMap<String, Boolean>, val itemsArray: ArrayList<Extra>, val context: Context) :
    RecyclerView.Adapter<ViewHolderCheckBox>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolderCheckBox {
        return ViewHolderCheckBox(LayoutInflater.from(context).inflate(R.layout.listitem_checkbox, p0, false))
    }

    override fun onBindViewHolder(p0: ViewHolderCheckBox, p1: Int) {
        var extra = itemsArray[p1]
        p0.checkboxExtra.text = extra?.name
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return itemsArray.size
    }
}

class ViewHolderCheckBox(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val checkboxExtra = view.checkboxExtra
}