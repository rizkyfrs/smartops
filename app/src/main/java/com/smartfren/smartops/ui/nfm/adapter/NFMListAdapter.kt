package com.smartfren.smartops.ui.nfm.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.nfm.NFMDetailActivity
import com.smartfren.smartops.utils.hide
import kotlinx.android.synthetic.main.nfm_list_item.view.*

class NFMListAdapter(private val context: Context) : RecyclerView.Adapter<NFMListAdapter.ViewHolder>() {
    private val mList: ArrayList<ItemTable> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.nfm_list_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        // sets the text to the textview from our itemHolder class
        if (position == mList.lastIndex) {
            holder.vLine.hide()
        }

        holder.tvName.text = item.typeTwo

        if (item.typeOne != "1") {
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }

        when (item.typeFour) {
            "0" -> {
                if (item.typeSix?.lowercase() == "general alarm" || item.typeSix?.contains("Genset") == true
                    || item.typeSix?.contains("CB LV GENSET") == true || item.typeTwo?.contains("CB IN PKG") == true) {
                    holder.swItemNFM.isChecked = true
                    holder.tvSwitchItemNFM.text = "Off"
                    holder.tvSwitchItemNFM.setTextColor(context.resources.getColor(R.color.color_propose_to_closed))
                } else {
                    holder.swItemNFM.isChecked = false
                    holder.tvSwitchItemNFM.text = "Off"
                    holder.tvSwitchItemNFM.setTextColor(context.resources.getColor(R.color.color_pink))
                }
            }
            "1" -> {
                if (item.typeTwo?.lowercase() == "general alarm" || item.typeSix?.contains("Genset") == true
                    || item.typeSix?.contains("CB LV GENSET") == true || item.typeTwo?.contains("CB IN PKG") == true) {
                    holder.swItemNFM.isChecked = false
                    holder.tvSwitchItemNFM.text = "On"
                    holder.tvSwitchItemNFM.setTextColor(context.resources.getColor(R.color.color_pink))
                } else {
                    holder.swItemNFM.isChecked = true
                    holder.tvSwitchItemNFM.text = "On"
                    holder.tvSwitchItemNFM.setTextColor(context.resources.getColor(R.color.color_propose_to_closed))
                }
            }
        }

        holder.itemLayout.setOnClickListener {
            when (item.typeOne) {
                "1" -> {
                    val intent = Intent(context, NFMDetailActivity::class.java)
                    intent.putExtra("titleNFMItem", "${item.typeTwo}")
                    intent.putExtra("idNFMItem", "${item.typeThree}")
                    intent.putExtra("cityNFMItem", "${item.typeFive}")
                    context.startActivity(intent)
                }
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val itemLayout: ConstraintLayout = ItemView.itemLayout
        val tvName: TextView = ItemView.tvItemNFM
        val tvSwitchItemNFM: TextView = ItemView.tvSwitchItemNFM
        val swItemNFM: Switch = ItemView.swItemNFM
        val vLine: View = ItemView.vLine
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