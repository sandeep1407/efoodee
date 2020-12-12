package com.uj.myapplications.adapters

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.uj.myapplications.R
import com.uj.myapplications.fragments.AddMenuFragment
import com.uj.myapplications.pojo.MenuPojo.MenuDetailPojo
import com.uj.myapplications.utility.RestTags
import com.uj.myapplications.utility.UtilityClass
import android.support.v7.app.AppCompatActivity


public class MutipleAdapter(
    val context: Context,
    var menuList: List<MenuDetailPojo>, var typeOfMenu: String
) : RecyclerView.Adapter<MutipleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): MutipleAdapter.ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        return when (viewType) {
            TYPE_CURRENT, TYPE_SCHEDULED -> ViewHolderItemA(
                inflater.inflate(
                    R.layout.listitem_edit_menu_current,
                    null
                )
            )
            //  else -> ViewHolderItemB(inflater.inflate(R.layout.listitem_menu_only_show, null))
            else -> ViewHolderItemB(
                inflater.inflate(
                    R.layout.listitem_edit_menu_current,
                    null
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: MutipleAdapter.ViewHolder, p1: Int) {
        var menuDetailPojo = menuList[p1]
        when (typeOfMenu) {
            RestTags.CURRENT, RestTags.SCHEDULED -> {
                val holderA = holder as ViewHolderItemA
                holderA.textViewMenuName.text = menuDetailPojo.name.nameEnglish
                holderA.textViewMenuDate.text = menuDetailPojo.scheduledTo.toString()
                holderA.textViewMenuType.text = menuDetailPojo.mealType.toString()
                when (menuDetailPojo.mealType) {
                    0, 1, 2 -> {
                        holderA.textViewMenuType.text = "Lunch"
                    }
                    else -> {
                        holderA.textViewMenuType.text = "Dinner"
                    }
                }
                holderA.editMenu.setOnClickListener {
                    //GoTo Add Menu Frag and Hit Api of Get Menu Details
                    // Update and Then Hit Update Menu
                    val fragment = AddMenuFragment()
                    val bundle = Bundle()
                    bundle.putString("menuID", menuDetailPojo.id)
                    fragment.arguments = bundle
                    AddMenuFragment.caller = "EDIT"
                    val activity = context as Activity
                    val manager = (context as AppCompatActivity).supportFragmentManager
                    // var fragManager = context.supportFragmentManager
                    UtilityClass.switchToFragment(fragment, "MyProfileFrag", R.id.fragment_container, manager, true)
                    // UtilityClass.switchToFragment(fragment, "Title", R.id.fr)
                    //UtilityClass.showToast(context, "Work in progress")
                }
            }
            RestTags.RECENT -> {
                val holderB = holder as ViewHolderItemB
                holderB.textViewMenuName.text = menuDetailPojo.name.nameEnglish.toString()
                /* holderB.textViewMenuDate.text = UtilityClass.changeDateToFormat(
                     RestTags.DATE_INPUT_FORMAT_HOURS,
                     RestTags.DATE_OUTPUT_FORMAT,
                     menuDetailPojo.scheduledTo.toString()
                 )*/
                holderB.textViewMenuDate.text =
                        menuDetailPojo.scheduledTo.toString()

                holderB.editMenu.setOnClickListener {
                    //GoTo Add Menu Frag and Hit Api of Get Menu Details
                    // Update and Then Hit Update Menu
                    val fragment = AddMenuFragment()
                    val bundle = Bundle()
                    bundle.putString("menuID", menuDetailPojo.id)
                    fragment.arguments = bundle
                    AddMenuFragment.caller = "EDIT"
                    val activity = context as Activity
                    val manager = (context as AppCompatActivity).supportFragmentManager
                    // var fragManager = context.supportFragmentManager
                    UtilityClass.switchToFragment(fragment, "MyProfileFrag", R.id.fragment_container, manager, true)
                    // UtilityClass.switchToFragment(fragment, "Title", R.id.fr)
                    //UtilityClass.showToast(context, "Work in progress")
                }
                if (menuDetailPojo.mealType != null) {
                    when (menuDetailPojo.mealType) {
                        0, 1, 2 -> {
                            holderB.textViewMenuType.text = "Lunch"

                        }
                        else -> {
                            holderB.textViewMenuType.text = "Dinner"
                        }
                    }
                    //  holderB.textViewMenuType.text = menuDetailPojo.mealType.toString()
                }

                //holderB.textViewMenuType.text = menuDetailPojo.mealType.toString()
            }
        }
    }

    companion object {
        const val TYPE_CURRENT = 0
        const val TYPE_RECENT = 1
        const val TYPE_SCHEDULED = 2


    }

    override fun getItemViewType(position: Int): Int {
        when (typeOfMenu) {
            RestTags.CURRENT -> {
                return TYPE_CURRENT
            }
            RestTags.RECENT -> {
                return TYPE_RECENT
            }
            RestTags.SCHEDULED -> {
                return TYPE_SCHEDULED
            }

        }
        return TYPE_CURRENT
    }


    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderItemA(itemView: View) : ViewHolder(itemView) {
        val textViewMenuName: TextView = itemView.findViewById(R.id.txt_menu_name)
        val textViewMenuDate: TextView = itemView.findViewById(R.id.txt_menu_date)
        val textViewMenuType: TextView = itemView.findViewById(R.id.txt_menu_type)
        var editMenu: Button = itemView.findViewById(R.id.btn_edit_menu)


    }

    inner class ViewHolderItemB(itemView: View) : ViewHolder(itemView) {
        val textViewMenuName: TextView = itemView.findViewById(R.id.txt_menu_name)
        val textViewMenuDate: TextView = itemView.findViewById(R.id.txt_menu_date)
        val textViewMenuType: TextView = itemView.findViewById(R.id.txt_menu_type)
        var editMenu: Button = itemView.findViewById(R.id.btn_edit_menu)
    }


}

