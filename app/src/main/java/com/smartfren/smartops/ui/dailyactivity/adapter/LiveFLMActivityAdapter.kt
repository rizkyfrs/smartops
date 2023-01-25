package com.smartfren.smartops.ui.dailyactivity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.LiveFLMActivityListResponse
import com.smartfren.smartops.data.models.ScheduleTaskListDetailResponse
import com.smartfren.smartops.ui.dailyactivity.ScheduleViewTaskDetailActivity
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.android.synthetic.main.item_task.view.itemLayoutSchedule
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class LiveFLMActivityAdapter(private val context: Context) :
    RecyclerView.Adapter<LiveFLMActivityAdapter.ViewHolder>() {
    private val mList: ArrayList<LiveFLMActivityListResponse.MvDactlivemon> = ArrayList()

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
        holder.itemLayoutItemOneEnd.hide()
        holder.itemIndicatorTwo.hide()
        holder.itemTextFour.show()
        holder.itemTextOne.text = item.dactSiteId
        holder.itemTextTwo.text = item.dactPic
        holder.itemTextThree.text = item.dactDomain
        holder.itemTextFour.text = item.dactActivityName
        holder.imgPriority.show()

        when (item.dactPriority) {
            "P0" -> {
                holder.imgPriority.setBackgroundResource(R.drawable.bg_p_uncategory)
                holder.imgPriority.backgroundTintList =
                    context.resources.getColorStateList(R.color.color_p0)
            }
            "P1" -> {
                holder.imgPriority.setBackgroundResource(R.drawable.bg_p_uncategory)
                holder.imgPriority.backgroundTintList =
                    context.resources.getColorStateList(R.color.color_p1)
            }
            "P2" -> {
                holder.imgPriority.setBackgroundResource(R.drawable.bg_p_uncategory)
                holder.imgPriority.backgroundTintList =
                    context.resources.getColorStateList(R.color.color_p2)
            }
            "" -> {
                holder.imgPriority.setBackgroundResource(R.drawable.bg_p_uncategory)
            }
        }
        when (item.dactStatus) {
            "PROPOSE TO CLOSE" -> {
                holder.itemIndicatorOne.backgroundTintList = context.resources.getColorStateList(R.color.color_propose_to_closed)
                holder.itemIndicatorOne.text = context.getString(R.string.propose_to_close)
                holder.itemLayoutIndicator.show()
            }
            "PENDING" -> {
                holder.itemIndicatorOne.backgroundTintList = context.resources.getColorStateList(R.color.color_reject)
                holder.itemIndicatorOne.text = context.getString(R.string.pending)
                holder.itemLayoutIndicator.show()
            }
            "REJECT" -> {
                holder.itemIndicatorOne.backgroundTintList = context.resources.getColorStateList(R.color.color_red_down)
                holder.itemIndicatorOne.text = context.getString(R.string.reject)
                holder.itemLayoutIndicator.show()
            }
            "CLOSED" -> {
                holder.itemIndicatorOne.backgroundTintList = context.resources.getColorStateList(R.color.color_closed)
                holder.itemIndicatorOne.text = context.getString(R.string.closed)
                holder.itemLayoutIndicator.show()
            }
            "SCHEDULED" -> {
                holder.itemLayoutIndicator.hide()
            }
        }
        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, ScheduleViewTaskDetailActivity::class.java)
            intent.putExtra("liveFLM", "yes")
            intent.putExtra("site", item.dactSiteId)
            intent.putExtra("activityNumber", item.dactNum)
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
        val itemLayoutIndicator: LinearLayoutCompat = ItemView.layoutIndicator
        val itemIndicatorOne: TextView = ItemView.indicatorOne
        val itemIndicatorTwo: TextView = ItemView.indicatorTwo
        val itemLayoutItemOneEnd: TextView = ItemView.tvItemOneEnd
        val itemTextOne: TextView = ItemView.tvItemOneStart
        val itemTextTwo: TextView = ItemView.tvItemTwo
        val itemTextThree: TextView = ItemView.tvItemThree
        val itemTextFour: TextView = ItemView.tvItemFour
        val imgPriority: ImageView = ItemView.imgPriority
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<LiveFLMActivityListResponse.MvDactlivemon>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
