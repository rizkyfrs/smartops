package com.smartfren.smartops.ui.nfm.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.nfm.NFMListActivity
import com.smartfren.smartops.utils.show
import kotlinx.android.synthetic.main.nfm_home_item.view.*

class NFMAlarmAdapter(private val context: Context) : RecyclerView.Adapter<NFMAlarmAdapter.ViewHolder>() {
    private val mList: ArrayList<ItemTable> = ArrayList()

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
        holder.tvTime.show()
        holder.ivIconItem.setImageResource(R.drawable.ic_fire_no_color)
        holder.ivIconItem.setBackgroundColor(context.resources.getColor(R.color.color_pink))
        holder.tvName.text = item.typeOne
        holder.tvTime.text = item.typeTwo

        holder.itemLayout.setOnClickListener {
//            when (item.id) {
//                1 -> {
//                    val intent = Intent(context, NFMListActivity::class.java)
//                    intent.putExtra("cityNFMItem", "${item.name}")
//                    context.startActivity(intent)
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
        val ivIconItem: ImageView = ItemView.ivIconItem
        val tvName: TextView = ItemView.tvNFMHomeItem
        val tvTime: TextView = ItemView.tvSubNFMHomeItem
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