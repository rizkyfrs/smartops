package com.smartfren.smartops.ui.workordermanagement.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.IncidentWOListResponse
import com.smartfren.smartops.ui.workordermanagement.WorkorderDetailActivity
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class WOAdapter(private val context: Context) : RecyclerView.Adapter<WOAdapter.ViewHolder>() {
    private val mList: ArrayList<IncidentWOListResponse.TbIncidentWorkorder> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.itemLayoutIndicator.hide()
        holder.tvItemTitleOneStart.show()
        holder.tvItemTitleOneEnd.show()
        holder.tvItemTitleTwo.show()
        holder.tvItemTitleThree.show()
        holder.tvItemThree.setTypeface(null, Typeface.BOLD)

        holder.ivIconItem.setImageDrawable(context.resources.getDrawable(R.drawable.ic_confirmation))
        holder.ivIconItem.imageTintList = context.resources.getColorStateList(R.color.white)
        holder.tvItemTitleOneStart.text = "Ticket ID"
        holder.tvItemOneStart.text = "${item.incwoSrsTt}"
        holder.tvItemTitleOneEnd.text = "Status"
        holder.tvItemOneEnd.text = if (item.incwoStatus != null) item.incwoStatus else "-"
        holder.tvItemTitleTwo.text = "WO ID"
        holder.tvItemTwo.text = item.incwoWoid
        holder.tvItemTitleThree.text = "Site ID"
        holder.tvItemThree.text = item.incSiteId
        holder.tvItemTitleFour.text = "End Time"
        holder.tvItemFour.text = if (item.incwoEndtime != null) item.incwoEndtime else "-"
        holder.tvItemTitleFive.text = "Duration"
        holder.tvItemFive.text = if (item.incwoDuration != null) item.incwoDuration else "-"

        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, WorkorderDetailActivity::class.java)
            intent.putExtra("woID", "${item.incwoId}")
            context.startActivity(intent)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val itemLayout: CardView = ItemView.itemLayoutSchedule
        val ivIconItem: ImageView = ItemView.ivIconItem
        val tvItemTitleOneStart: TextView = ItemView.tvItemTitleOneStart
        val tvItemOneStart: TextView = ItemView.tvItemOneStart
        val tvItemTitleOneEnd: TextView = ItemView.tvItemTitleOneEnd
        val tvItemOneEnd: TextView = ItemView.tvItemOneEnd
        val tvItemTitleTwo: TextView = ItemView.tvItemTitleTwo
        val tvItemTwo: TextView = ItemView.tvItemTwo
        val tvItemTitleThree: TextView = ItemView.tvItemTitleThree
        val tvItemThree: TextView = ItemView.tvItemThree
        val tvItemTitleFour: TextView = ItemView.tvItemTitleFour
        val tvItemFour: TextView = ItemView.tvItemFour
        val tvItemTitleFive: TextView = ItemView.tvItemTitleFive
        val tvItemFive: TextView = ItemView.tvItemFive
        val imgPriority: ImageView = ItemView.imgPriority
        val itemLayoutIndicator: LinearLayoutCompat = ItemView.layoutIndicator
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<IncidentWOListResponse.TbIncidentWorkorder>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}