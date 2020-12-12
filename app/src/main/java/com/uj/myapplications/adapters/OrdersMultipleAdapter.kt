package com.uj.myapplications.adapters

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.uj.myapplications.R
import com.uj.myapplications.fragments.MyOrdersFragment
import com.uj.myapplications.fragments.ReviewsFragment
import com.uj.myapplications.pojo.MenuPojo.Extra
import com.uj.myapplications.pojo.OrderPojo.OrderDetailPojo
import com.uj.myapplications.utility.RestTags
import com.uj.myapplications.utility.UtilityClass

class OrdersMultipleAdapter(
    val context: Context,
    var orderList: List<OrderDetailPojo>, var typeOfOrder: String
) : RecyclerView.Adapter<OrdersMultipleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): OrdersMultipleAdapter.ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        when (MyOrdersFragment.ORDERPROGRESSSTATUS) {
            2, 3 -> {
                return when (viewType) {
                    TYPE_ALL_ORDERS -> ViewHolderItemOrdersAll(inflater.inflate(R.layout.listitem_orders, null))
                    TYPE_SELF_PICKUP -> ViewHolderItemOrdersSelfPickup(inflater.inflate(R.layout.listitem_orders, null))
                    TYPE_SELF_DELIVERY -> ViewHolderItemOrdersSelfDelivery(
                        inflater.inflate(
                            R.layout.listitem_orders,
                            null
                        )
                    )
                    TYPE_MYMA_DELIVERY -> ViewHolderItemOrdersMymaDelivery(
                        inflater.inflate(
                            R.layout.listitem_orders,
                            null
                        )
                    )
                    else -> ViewHolderItemOrdersAll(inflater.inflate(R.layout.listitem_orders, null))
                }
            }
            else -> {
                //Case Compplete Orders
                return when (viewType) {
                    TYPE_ALL_ORDERS -> ViewHolderItemOrdersComplete(
                        inflater.inflate(
                            R.layout.listitem_complete_orders,
                            null
                        )
                    )
                    TYPE_SELF_PICKUP -> ViewHolderItemOrdersComplete(
                        inflater.inflate(
                            R.layout.listitem_complete_orders,
                            null
                        )
                    )
                    TYPE_SELF_DELIVERY -> ViewHolderItemOrdersComplete(
                        inflater.inflate(
                            R.layout.listitem_complete_orders,
                            null
                        )
                    )
                    TYPE_MYMA_DELIVERY -> ViewHolderItemOrdersComplete(
                        inflater.inflate(
                            R.layout.listitem_complete_orders,
                            null
                        )
                    )
                    else -> ViewHolderItemOrdersComplete(inflater.inflate(R.layout.listitem_complete_orders, null))
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrdersMultipleAdapter.ViewHolder, p1: Int) {
        var orderDetailPojo = orderList[p1]

        when (MyOrdersFragment.ORDERPROGRESSSTATUS) {
            2, 3 -> {
                when (typeOfOrder) {
                    RestTags.DINE_IN_STR -> {
                        val holderC = holder as ViewHolderItemOrdersAll
                        var flagClick = false
                        val data = orderDetailPojo.customer
                        if (data != null) {
                            val name = data.name
                            if (name != null) {
                                holderC.textViewName.text = name.fname
                            } else {
                                holderC.textViewName.text = "N/A"
                            }
                        }
                        var map: HashMap<String, Boolean> = HashMap()
                        var list: ArrayList<Extra> = ArrayList()
                        holderC.textViewMenuDate.text = orderDetailPojo?.date
                        holderC.textViewPrice.text = "Rs." + orderDetailPojo?.bill
                        holderC.textViewOrderNumber.text = "Order No." + orderDetailPojo?.orderNumber
                        // holderC.textViewMenuName.text =
                        holderC.textViewBill.text = "Total bill Rs." + orderDetailPojo?.bill
                        holderC.btnMealReady.setOnClickListener {
                            UtilityClass.showToast(context, "Meal Ready")
                        }
                        holderC.rr_Extra.layoutManager = LinearLayoutManager(context)
                        holderC.rr_Extra.setHasFixedSize(true)
                        var meal = orderDetailPojo?.meal
                        if (meal != null) {
                            var menu = meal?.menu
                            if (menu != null) {
                                list.addAll(menu.extras)
                            }
                        }
                        map["Extra1"] = true
                        map["Extra2"] = true
                        map["Extra3"] = true
                        /* list.add("Extra1")
                         list.add("Extra2")
                         list.add("Extra3")*/
                        val adapter = CheckBoxAdapter(map, list, context)
                        holderC.rr_Extra.adapter = adapter
                        adapter.notifyDataSetChanged()
                        holderC.btnStartDelivery.setOnClickListener {
                            UtilityClass.showToast(context, "Start Delivery")
                        }
                        holderC.textViewName.setOnClickListener {
                            //Expand the view
                            if (!flagClick) {
                                holderC.llExpandView.visibility = View.VISIBLE
                                holderC.textViewPrice.visibility = View.GONE
                                flagClick = true
                            } else {
                                holderC.llExpandView.visibility = View.GONE
                                holderC.textViewPrice.visibility = View.VISIBLE
                                flagClick = false
                            }
                        }

                        holderC.textViewCallCustomer.setOnClickListener {
                            UtilityClass.showToast(context, "Call to Customer Module will be added")
                        }
                        holderC.textViewDirection.setOnClickListener {
                            UtilityClass.showToast(context, "Direction on Map Module will be added")
                        }
                    }

                    RestTags.MYMA_DELIVERY_STR -> {
                        val holderC = holder as ViewHolderItemOrdersMymaDelivery
                        var flagClick = false
                        val data = orderDetailPojo.customer
                        if (data != null) {
                            val name = data.name
                            if (name != null) {
                                holderC.textViewName.text = name.fname
                            } else {
                                holderC.textViewName.text = "N/A"
                            }
                        }
                        holderC.textViewMenuDate.text = orderDetailPojo?.date
                        holderC.textViewPrice.text = "Rs." + orderDetailPojo?.bill
                        holderC.textViewOrderNumber.text = "Order No." + orderDetailPojo?.orderNumber
                        // holderC.textViewMenuName.text =
                        holderC.textViewBill.text = "Total bill Rs." + orderDetailPojo?.bill
                        holderC.btnMealReady.setOnClickListener {
                            UtilityClass.showToast(context, "Meal Ready")
                        }
                        holderC.rr_Extra.layoutManager = LinearLayoutManager(context)
                        holderC.rr_Extra.setHasFixedSize(true)
                        var map: HashMap<String, Boolean> = HashMap()
                        var list: ArrayList<Extra> = ArrayList()
                        map["Extra1"] = true
                        map["Extra2"] = true
                        map["Extra3"] = true
                        /* list.add("Extra1")
                         list.add("Extra2")
                         list.add("Extra3")*/
                        var meal = orderDetailPojo?.meal
                        if (meal != null) {
                            var menu = meal?.menu
                            if (menu != null) {
                                list.addAll(menu.extras)
                            }
                        }
                        val adapter = CheckBoxAdapter(map, list, context)
                        holderC.rr_Extra.adapter = adapter
                        adapter.notifyDataSetChanged()
                        holderC.btnStartDelivery.setOnClickListener {
                            UtilityClass.showToast(context, "Start Delivery")
                        }
                        holderC.textViewName.setOnClickListener {
                            //Expand the view
                            if (!flagClick) {
                                holderC.llExpandView.visibility = View.VISIBLE
                                holderC.textViewPrice.visibility = View.GONE
                                flagClick = true
                            } else {
                                holderC.llExpandView.visibility = View.GONE
                                holderC.textViewPrice.visibility = View.VISIBLE
                                flagClick = false
                            }
                        }

                        holderC.textViewCallCustomer.setOnClickListener {
                            UtilityClass.showToast(context, "Call to Customer Module will be added")
                        }
                        holderC.textViewDirection.setOnClickListener {
                            UtilityClass.showToast(context, "Direction on Map Module will be added")
                        }
                    }
                    RestTags.SELF_DELIVERY_STR -> {
                        val holderC = holder as ViewHolderItemOrdersSelfDelivery
                        var flagClick = false
                        val data = orderDetailPojo.customer
                        if (data != null) {
                            val name = data.name
                            if (name != null) {
                                holderC.textViewName.text = name.fname
                            } else {
                                holderC.textViewName.text = "N/A"
                            }
                        }
                        holderC.textViewMenuDate.text = orderDetailPojo?.date
                        holderC.textViewPrice.text = "Rs." + orderDetailPojo?.bill
                        holderC.textViewOrderNumber.text = "Order No." + orderDetailPojo?.orderNumber
                        // holderC.textViewMenuName.text =
                        holderC.textViewBill.text = "Total bill Rs." + orderDetailPojo?.bill
                        holderC.btnMealReady.setOnClickListener {
                            UtilityClass.showToast(context, "Meal Ready")
                        }
                        holderC.rr_Extra.layoutManager = LinearLayoutManager(context)
                        holderC.rr_Extra.setHasFixedSize(true)
                        var map: HashMap<String, Boolean> = HashMap()
                        var list: ArrayList<Extra> = ArrayList()
                        map["Extra1"] = true
                        map["Extra2"] = true
                        map["Extra3"] = true
                        /* list.add("Extra1")
                         list.add("Extra2")
                         list.add("Extra3")*/
                        var meal = orderDetailPojo?.meal
                        if (meal != null) {
                            var menu = meal?.menu
                            if (menu != null) {
                                list.addAll(menu.extras)
                            }
                        }
                        val adapter = CheckBoxAdapter(map, list, context)
                        holderC.rr_Extra.adapter = adapter
                        adapter.notifyDataSetChanged()
                        holderC.btnStartDelivery.setOnClickListener {
                            UtilityClass.showToast(context, "Start Delivery")
                        }
                        holderC.textViewName.setOnClickListener {
                            //Expand the view
                            if (!flagClick) {
                                holderC.llExpandView.visibility = View.VISIBLE
                                holderC.textViewPrice.visibility = View.GONE
                                flagClick = true
                            } else {
                                holderC.llExpandView.visibility = View.GONE
                                holderC.textViewPrice.visibility = View.VISIBLE
                                flagClick = false
                            }
                        }

                        holderC.textViewCallCustomer.setOnClickListener {
                            UtilityClass.showToast(context, "Call to Customer Module will be added")
                        }
                        holderC.textViewDirection.setOnClickListener {
                            UtilityClass.showToast(context, "Direction on Map Module will be added")
                        }
                    }

                    RestTags.SELF_PICKUP_STR -> {
                        val holderC = holder as ViewHolderItemOrdersSelfPickup
                        var flagClick = false
                        val data = orderDetailPojo.customer
                        if (data != null) {
                            val name = data.name
                            if (name != null) {
                                holderC.textViewName.text = name.fname
                            } else {
                                holderC.textViewName.text = "N/A"
                            }
                        }
                        holderC.textViewMenuDate.text = orderDetailPojo?.date
                        holderC.textViewPrice.text = "Rs." + orderDetailPojo?.bill
                        holderC.textViewOrderNumber.text = "Order No." + orderDetailPojo?.orderNumber
                        // holderC.textViewMenuName.text =
                        holderC.textViewBill.text = "Total bill Rs." + orderDetailPojo?.bill
                        holderC.btnMealReady.setOnClickListener {
                            UtilityClass.showToast(context, "Meal Ready")
                        }

                        holderC.rr_Extra.layoutManager = LinearLayoutManager(context)
                        holderC.rr_Extra.setHasFixedSize(true)
                        var map: HashMap<String, Boolean> = HashMap()
                        var list: ArrayList<Extra> = ArrayList()
                        map["Extra1"] = true
                        map["Extra2"] = true
                        map["Extra3"] = true
                        /* list.add("Extra1")
                         list.add("Extra2")
                         list.add("Extra3")*/
                        var meal = orderDetailPojo?.meal
                        if (meal != null) {
                            var menu = meal?.menu
                            if (menu != null) {
                                list.addAll(menu.extras)
                            }
                        }
                        val adapter = CheckBoxAdapter(map, list, context)
                        holderC.rr_Extra.adapter = adapter
                        adapter.notifyDataSetChanged()
                        holderC.btnStartDelivery.setOnClickListener {
                            UtilityClass.showToast(context, "Start Delivery")
                        }
                        holderC.textViewName.setOnClickListener {
                            //Expand the view
                            if (!flagClick) {
                                holderC.llExpandView.visibility = View.VISIBLE
                                holderC.textViewPrice.visibility = View.GONE
                                flagClick = true
                            } else {
                                holderC.llExpandView.visibility = View.GONE
                                holderC.textViewPrice.visibility = View.VISIBLE
                                flagClick = false
                            }
                        }

                        holderC.textViewCallCustomer.setOnClickListener {
                            UtilityClass.showToast(context, "Call to Customer Module will be added")
                        }
                        holderC.textViewDirection.setOnClickListener {
                            UtilityClass.showToast(context, "Direction on Map Module will be added")
                        }
                    }
                }
            }
            else -> {
                //Case Complete
                val holderC = holder as ViewHolderItemOrdersComplete
                val data = orderDetailPojo.customer
                if (data != null) {
                    val name = data.name
                    if (name != null) {
                        holderC.textViewName.text = name.fname
                    } else {
                        holderC.textViewName.text = "N/A"
                    }
                }
                holderC.textViewMenuDate.text = orderDetailPojo?.date
                holderC.btnAskForReview.setOnClickListener {
                    // Hit The Api Send It to Reviews Frgament
                    UtilityClass.showToast(context, "Ask for review! Work in progress")
                    var fragment = ReviewsFragment()
                    val bundle = Bundle()
                    bundle.putString("orderId", orderDetailPojo?.id)
                    bundle.putString(RestTags.FROM, "order")
                    fragment.arguments = bundle
                   // val activity = context as Activity
                    val manager = (context as AppCompatActivity).supportFragmentManager
                    UtilityClass.switchToFragment(fragment, "Review", R.id.fragment_container, manager, true)
                }
            }
        }

    }

    companion object {
        //Orders Data
        const val TYPE_ALL_ORDERS = 201
        const val TYPE_SELF_PICKUP = 202
        const val TYPE_SELF_DELIVERY = 203
        const val TYPE_MYMA_DELIVERY = 204
    }

    override fun getItemViewType(position: Int): Int {
        when (typeOfOrder) {
            RestTags.DINE_IN_STR -> {
                return TYPE_ALL_ORDERS
            }
            RestTags.SELF_PICKUP_STR -> {
                return TYPE_SELF_PICKUP
            }
            RestTags.SELF_DELIVERY_STR -> {
                return TYPE_SELF_DELIVERY
            }
            RestTags.MYMA_DELIVERY_STR -> {
                return TYPE_MYMA_DELIVERY
            }
        }
        return TYPE_ALL_ORDERS
    }


    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    // View Orders All
    inner class ViewHolderItemOrdersAll(itemView: View) : ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.txt_user_name)
        val textViewMenuDate: TextView = itemView.findViewById(R.id.txt_dateTime)
        val textViewPrice: TextView = itemView.findViewById(R.id.txt_price)
        val textViewType: TextView = itemView.findViewById(R.id.txt_service_type)
        val textViewOrderNumber: TextView = itemView.findViewById(R.id.txt_order_no)
        val textViewBill: TextView = itemView.findViewById(R.id.txt_bill)
        val textViewMenuName: TextView = itemView.findViewById(R.id.txt_menu_name)
        val textViewCallCustomer: TextView = itemView.findViewById(R.id.txtCallCustomer)
        val textViewDirection: TextView = itemView.findViewById(R.id.txtDirections)
        val btnMealReady: TextView = itemView.findViewById(R.id.btnMealReady)
        val btnStartDelivery: TextView = itemView.findViewById(R.id.btnStartDelivery)
        val llExpandView: LinearLayout = itemView.findViewById(R.id.llExpand)
        val rr_Extra: RecyclerView = itemView.findViewById(R.id.rr_lisitem_extra)

    }

    // View Orders Self-Pickup
    inner class ViewHolderItemOrdersSelfPickup(itemView: View) : ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.txt_user_name)
        val textViewMenuDate: TextView = itemView.findViewById(R.id.txt_dateTime)
        val textViewPrice: TextView = itemView.findViewById(R.id.txt_price)
        val textViewType: TextView = itemView.findViewById(R.id.txt_service_type)
        val textViewOrderNumber: TextView = itemView.findViewById(R.id.txt_order_no)
        val textViewBill: TextView = itemView.findViewById(R.id.txt_bill)
        val textViewMenuName: TextView = itemView.findViewById(R.id.txt_menu_name)
        val textViewCallCustomer: TextView = itemView.findViewById(R.id.txtCallCustomer)
        val textViewDirection: TextView = itemView.findViewById(R.id.txtDirections)
        val btnMealReady: TextView = itemView.findViewById(R.id.btnMealReady)
        val btnStartDelivery: TextView = itemView.findViewById(R.id.btnStartDelivery)
        val llExpandView: LinearLayout = itemView.findViewById(R.id.llExpand)
        val rr_Extra: RecyclerView = itemView.findViewById(R.id.rr_lisitem_extra)

    }

    // View Orders Self-Delivery
    inner class ViewHolderItemOrdersSelfDelivery(itemView: View) : ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.txt_user_name)
        val textViewMenuDate: TextView = itemView.findViewById(R.id.txt_dateTime)
        val textViewPrice: TextView = itemView.findViewById(R.id.txt_price)
        val textViewType: TextView = itemView.findViewById(R.id.txt_service_type)
        val textViewOrderNumber: TextView = itemView.findViewById(R.id.txt_order_no)
        val textViewBill: TextView = itemView.findViewById(R.id.txt_bill)
        val textViewMenuName: TextView = itemView.findViewById(R.id.txt_menu_name)
        val textViewCallCustomer: TextView = itemView.findViewById(R.id.txtCallCustomer)
        val textViewDirection: TextView = itemView.findViewById(R.id.txtDirections)
        val btnMealReady: TextView = itemView.findViewById(R.id.btnMealReady)
        val btnStartDelivery: TextView = itemView.findViewById(R.id.btnStartDelivery)
        val llExpandView: LinearLayout = itemView.findViewById(R.id.llExpand)
        val rr_Extra: RecyclerView = itemView.findViewById(R.id.rr_lisitem_extra)

    }

    // View Orders Myma-Delivery
    inner class ViewHolderItemOrdersMymaDelivery(itemView: View) : ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.txt_user_name)
        val textViewMenuDate: TextView = itemView.findViewById(R.id.txt_dateTime)
        val textViewPrice: TextView = itemView.findViewById(R.id.txt_price)
        val textViewType: TextView = itemView.findViewById(R.id.txt_service_type)
        val textViewOrderNumber: TextView = itemView.findViewById(R.id.txt_order_no)
        val textViewBill: TextView = itemView.findViewById(R.id.txt_bill)
        val textViewMenuName: TextView = itemView.findViewById(R.id.txt_menu_name)
        val textViewCallCustomer: TextView = itemView.findViewById(R.id.txtCallCustomer)
        val textViewDirection: TextView = itemView.findViewById(R.id.txtDirections)
        val btnMealReady: TextView = itemView.findViewById(R.id.btnMealReady)
        val btnStartDelivery: TextView = itemView.findViewById(R.id.btnStartDelivery)
        val llExpandView: LinearLayout = itemView.findViewById(R.id.llExpand)
        val rr_Extra: RecyclerView = itemView.findViewById(R.id.rr_lisitem_extra)

    }

    // View Orders Comptleted
    inner class ViewHolderItemOrdersComplete(itemView: View) : ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.txt_user_name)
        val textViewMenuDate: TextView = itemView.findViewById(R.id.txt_dateTime)
        val btnAskForReview: Button = itemView.findViewById(R.id.btnAskForReview)

    }

}

