package com.smartfren.smartops.ui.reportactivity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ActivityReportResponse
import com.smartfren.smartops.ui.reportactivity.ActivityChecklistActivity
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.parseDateFromBackend
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ActivityReportAdapter(private val context: Context) :
    RecyclerView.Adapter<ActivityReportAdapter.ViewHolder>() {
    private val mList: ArrayList<ActivityReportResponse.Activity> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.tvItemThree.hide()
        holder.itemIndicator.hide()
        holder.tvActivityName.text = item.activityType
        holder.tvSite.text = item.siteID
        if (item.activityDate != null && item.activityDate != "0000-00-00") {
            holder.tvDate.text = parseDateFromBackend(item.activityDate.toString())
        } else {
            holder.tvDate.text = "-"
        }
        if (item.activityStatus != null) {
            when (item.activityStatus) {
                "DONE" -> {
                    holder.imgPriority.setBackgroundResource(R.drawable.bg_close)
                    holder.imgPriority.backgroundTintList =
                        context.resources.getColorStateList(R.color.color_green_done)
                }
                "NOT DONE" -> {
                    holder.imgPriority.setBackgroundResource(R.drawable.bg_close)
                    holder.imgPriority.backgroundTintList =
                        context.resources.getColorStateList(R.color.color_red_down)
                }
                "" -> {
                    holder.imgPriority.setBackgroundResource(R.drawable.bg_close)
                    holder.imgPriority.backgroundTintList =
                        context.resources.getColorStateList(R.color.color_closed)
                }
            }
        }
        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, ActivityChecklistActivity::class.java)
            intent.putExtra("formNum", item.activityNum)
            intent.putExtra("actID", item.activityID)
            intent.putExtra("actNumID", item.activityNum)
            intent.putExtra("nameActivity", item.activityType)
            intent.putExtra("siteID", item.siteID)
            if (item.activityDate != null && item.activityDate != "0000-00-00") {
                intent.putExtra("dateActivity", parseDateFromBackend(item.activityDate.toString()))
            } else {
                intent.putExtra("dateActivity", "-")
            }
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
        val itemIndicator: LinearLayoutCompat = ItemView.layoutIndicator
        val tvActivityName: TextView = ItemView.tvItemTwo
        val tvItemThree: TextView = ItemView.tvItemThree
        val tvSite: TextView = ItemView.tvItemOneStart
        val tvDate: TextView = ItemView.tvItemOneEnd
        val imgPriority: ImageView = ItemView.imgPriority
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<ActivityReportResponse.Activity>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
