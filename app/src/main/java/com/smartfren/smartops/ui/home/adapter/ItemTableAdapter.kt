package com.smartfren.smartops.ui.home.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.utils.hide
import kotlinx.android.synthetic.main.item_network_inventory.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ItemTableAdapter(private val context: Context) :
    RecyclerView.Adapter<ItemTableAdapter.ViewHolder>() {
    private val mList: ArrayList<ItemTable> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_network_inventory, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]

        if (position == mList.lastIndex) {
            holder.vwLine.hide()
        }

        // sets the text to the textview from our itemHolder class
        if (item.textStyle == "bold") {
            holder.tvItemOne.setTypeface(null, Typeface.BOLD)
            holder.tvItemTwo.setTypeface(null, Typeface.BOLD)
            holder.tvItemThree.setTypeface(null, Typeface.BOLD)
            holder.tvItemFour.setTypeface(null, Typeface.BOLD)
            holder.tvItemFive.setTypeface(null, Typeface.BOLD)
            holder.tvItemSix.setTypeface(null, Typeface.BOLD)
            holder.tvItemSeven.setTypeface(null, Typeface.BOLD)
            holder.tvItemEight.setTypeface(null, Typeface.BOLD)
            holder.tvItemNine.setTypeface(null, Typeface.BOLD)
            holder.tvItemTen.setTypeface(null, Typeface.BOLD)
        }

        holder.tvItemOne.setTextColor(context.resources.getColor(item.textColor))
        holder.tvItemTwo.setTextColor(context.resources.getColor(item.textColor))
        holder.tvItemThree.setTextColor(context.resources.getColor(item.textColor))
        holder.tvItemFour.setTextColor(context.resources.getColor(item.textColor))
        holder.tvItemFive.setTextColor(context.resources.getColor(item.textColor))
        holder.tvItemSix.setTextColor(context.resources.getColor(item.textColor))
        holder.tvItemSeven.setTextColor(context.resources.getColor(item.textColor))
        holder.tvItemEight.setTextColor(context.resources.getColor(item.textColor))
        holder.tvItemNine.setTextColor(context.resources.getColor(item.textColor))
        holder.tvItemTen.setTextColor(context.resources.getColor(item.textColor))

        holder.tvItemOne.text = item.typeOne
        holder.tvItemTwo.text = item.typeTwo
        if (item.typeThree != "") {
            holder.tvItemThree.text = item.typeThree
        } else {
            holder.tvItemThree.hide()
        }
        if (item.typeFour != "") {
            holder.tvItemFour.text = item.typeFour
        } else {
            holder.tvItemFour.hide()
        }

        if (item.typeFive != "") {
            holder.tvItemFive.text = item.typeFive
        } else {
            holder.tvItemFive.hide()
        }

        if (item.typeSix != "") {
            holder.tvItemSix.text = item.typeSix
        } else {
            holder.tvItemSix.hide()
        }

        if (item.typeSeven != "") {
            holder.tvItemSeven.text = item.typeSeven
        } else {
            holder.tvItemSeven.hide()
        }

        if (item.typeEight != "") {
            holder.tvItemEight.text = item.typeEight
        } else {
            holder.tvItemEight.hide()
        }

        if (item.typeNine != "") {
            holder.tvItemNine.text = item.typeNine
        } else {
            holder.tvItemNine.hide()
        }

        if (item.typeTen != "") {
            holder.tvItemTen.text = item.typeTen
        } else {
            holder.tvItemTen.hide()
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvItemOne: TextView = ItemView.tvItemOne
        val tvItemTwo: TextView = ItemView.tvItemTwo
        val tvItemThree: TextView = ItemView.tvItemThree
        val tvItemFour: TextView = ItemView.tvItemFour
        val tvItemFive: TextView = ItemView.tvItemFive
        val tvItemSix: TextView = ItemView.tvItemSix
        val tvItemSeven: TextView = ItemView.tvItemSeven
        val tvItemEight: TextView = ItemView.tvItemEight
        val tvItemNine: TextView = ItemView.tvItemNine
        val tvItemTen: TextView = ItemView.tvItemTen
        val vwLine: View = ItemView.vwLine
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<ItemTable>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
