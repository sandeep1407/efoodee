package com.uj.myapplications.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.uj.myapplications.R


class DashboardAdapter(var context: Context, var listName: ArrayList<Any?>) : BaseAdapter() {
    var inflter: LayoutInflater? = null

    init {
        this.inflter = LayoutInflater.from(context)
    }

    override fun getItem(position: Int): Any? {
        return listName[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listName!!.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val vh: ListRowHolder
        if (convertView == null) {
            view = this.inflter!!.inflate(R.layout.grid_itemview, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.label.text = listName[position].toString()
        return view
    }

    private class ListRowHolder(row: View?) {
        public val label: TextView

        init {
            this.label = row?.findViewById(R.id.txt_title) as TextView
        }
    }
}