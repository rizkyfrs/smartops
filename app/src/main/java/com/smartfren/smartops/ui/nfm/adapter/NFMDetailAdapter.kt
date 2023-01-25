package com.smartfren.smartops.ui.nfm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.data.models.ItemTable
import kotlinx.android.synthetic.main.nfm_detail_item.view.*

class NFMDetailAdapter(private val context: Context) : RecyclerView.Adapter<NFMDetailAdapter.ViewHolder>() {
    private val mList: ArrayList<ItemTable> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.nfm_detail_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        // sets the text to the textview from our itemHolder class
//        if (position == mList.lastIndex) {
//            holder.vLine.hide()
//        }

        holder.tvNameNFMitem.text = item.typeOne
        holder.tvDateNFMitem.text = item.typeTwo
        holder.tvStatusNFMitem.text = item.typeThree
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvNameNFMitem: TextView = ItemView.tvNFMTitleDetail
        val tvDateNFMitem: TextView = ItemView.tvNFMDateDetail
        val tvStatusNFMitem: TextView = ItemView.tvNFMStatusDetail
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