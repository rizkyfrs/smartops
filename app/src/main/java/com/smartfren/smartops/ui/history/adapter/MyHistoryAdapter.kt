package com.smartfren.smartops.ui.history.adapter

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
import com.smartfren.smartops.data.models.ActivityHistoryLogResponse
import com.smartfren.smartops.ui.dailyactivity.ScheduleViewTaskDetailActivity
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.parseDateFromBackend
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MyHistoryAdapter(private val context: Context) :
    RecyclerView.Adapter<MyHistoryAdapter.ViewHolder>() {
    private val mList: ArrayList<ActivityHistoryLogResponse.Task> = ArrayList()

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
//        holder.tvTitleTask.visibility = View.GONE
//        holder.clTask.visibility = View.GONE
//        holder.tvTitleSite.visibility = View.VISIBLE
//        holder.tvTitleDate.visibility = View.VISIBLE
//        holder.tvTitleDomLev1.visibility = View.VISIBLE
//        holder.tvDactDomLev1.visibility = View.VISIBLE
//        holder.tvTitleDomLev2.visibility = View.VISIBLE
//        holder.tvDactDomLev2.visibility = View.VISIBLE
//        holder.imgPriority.visibility = View.VISIBLE
        holder.itemLayoutIndicator.hide()
        holder.tvSite.text = item.siteID
        if (item.actualDate != null) holder.tvDate.text = parseDateFromBackend(item.actualDate!!)
        holder.tvDactDomLev1.text = item.domainLev1
        holder.tvDactDomLev2.text = item.domainLev2
        if (item.taskStatus != null) {
            when (item.taskStatus) {
                "PROPOSE TO CLOSE" -> {
                    holder.imgPriority.setBackgroundResource(R.drawable.bg_propose_closed)
                    holder.imgPriority.backgroundTintList =
                        context.resources.getColorStateList(R.color.color_propose_to_closed)
                }
                "CLOSED" -> {
                    holder.imgPriority.setBackgroundResource(R.drawable.bg_close)
                    holder.imgPriority.backgroundTintList =
                        context.resources.getColorStateList(R.color.color_closed)
                }
            }
        }

        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, ScheduleViewTaskDetailActivity::class.java)
            intent.putExtra("history", "true")
            intent.putExtra("site", item.siteID)
            intent.putExtra("activityNumber", item.actNum)
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
//        val tvTitleSite: TextView = ItemView.tvsit
        val tvSite: TextView = ItemView.tvItemOneStart
//        val tvTitleDate: TextView = ItemView.tvTitleDateSch
        val tvDate: TextView = ItemView.tvItemOneEnd
//        val clTask: ConstraintLayout = ItemView.clTask
//        val tvTitleTask: TextView = ItemView.tvTitleTask
//        val tvTitleDomLev1: TextView = ItemView.
        val tvDactDomLev1: TextView = ItemView.tvItemTwo
//        val tvTitleDomLev2: TextView = ItemView.tvTitleDomLev2
        val tvDactDomLev2: TextView = ItemView.tvItemThree
        val imgPriority: ImageView = ItemView.imgPriority
        val itemLayoutIndicator: LinearLayoutCompat = ItemView.layoutIndicator
//        val bgStatus: ImageView = ItemView.bgStatus
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<ActivityHistoryLogResponse.Task>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
