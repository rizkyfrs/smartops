package com.smartfren.smartops.ui.nfm.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.nfm.NFMListActivity
import kotlinx.android.synthetic.main.nfm_home_item.view.*

class NFMHomeAdapter(private val context: Context) : RecyclerView.Adapter<NFMHomeAdapter.ViewHolder>() {
    private val mList: ArrayList<ContentMenu> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.nfm_home_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.tvName.text = item.name

        holder.itemLayout.setOnClickListener {
//            when (item.id) {
//                1 -> {
                    val intent = Intent(context, NFMListActivity::class.java)
                    intent.putExtra("cityNFMItem", "${item.name}")
                    context.startActivity(intent)
//                }
//                2 -> {
//
//                }
//            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val itemLayout: CardView = ItemView.itemLayout
        val tvName: TextView = ItemView.tvNFMHomeItem
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<ContentMenu>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}