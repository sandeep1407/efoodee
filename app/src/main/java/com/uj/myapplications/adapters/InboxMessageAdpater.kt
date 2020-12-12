package com.uj.myapplications.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup


import android.view.View
import com.uj.myapplications.R
import kotlinx.android.synthetic.main.message_list.view.*


class InboxMessageAdpater(val items : ArrayList<String> ,val date : ArrayList<String>, val context: Context) : RecyclerView.Adapter<InboxViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): InboxViewHolder {
        return InboxViewHolder(LayoutInflater.from(context).inflate(R.layout.message_list, p0, false))
    }

    override fun onBindViewHolder(holder: InboxViewHolder, position: Int) {
        holder.message.text = items.get(position)
        holder.date.text = date.get(position)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

}

class InboxViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val message = view.tvMessage
    val date = view.tvTime

}