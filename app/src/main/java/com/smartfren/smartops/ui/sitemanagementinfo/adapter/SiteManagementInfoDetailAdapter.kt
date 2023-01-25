package com.smartfren.smartops.ui.sitemanagementinfo.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import kotlinx.android.synthetic.main.item_title_context.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SiteManagementInfoDetailAdapter(private val context: Context) :
    RecyclerView.Adapter<SiteManagementInfoDetailAdapter.ViewHolder>() {
    private val mList: ArrayList<OptionModel> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_title_context, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.tvTitleContent.text = item.id
        if (item.type == null) {
            holder.tvDescContent.text = "-"
        } else {
            holder.tvDescContent.text = item.type
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvTitleContent: TextView = ItemView.tvTitleContent
        val tvDescContent: TextView = ItemView.tvDescContent
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<OptionModel>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
