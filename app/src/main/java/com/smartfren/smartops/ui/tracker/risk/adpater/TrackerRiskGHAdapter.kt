package com.smartfren.smartops.ui.tracker.risk.adpater

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
import com.smartfren.smartops.data.models.TrackerRiskGHListResponse
import com.smartfren.smartops.data.models.TrackerRiskListResponse
import com.smartfren.smartops.ui.tracker.risk.TrackerRiskDetailActivity
import com.smartfren.smartops.utils.hide
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TrackerRiskGHAdapter(private val context: Context) :
    RecyclerView.Adapter<TrackerRiskGHAdapter.ViewHolder>() {
    private val mList: ArrayList<TrackerRiskGHListResponse.CvRegrtrackerVal> = ArrayList()

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
        holder.indicatorTwo.hide()

        when (item.ttStatus) {
            "Open" -> {
                holder.statusIndicator.text = item.ttStatus
                holder.statusIndicator.backgroundTintList =
                    context.resources.getColorStateList(R.color.color_red_down)
            }
            "Resolve" -> {
                holder.statusIndicator.text = item.ttStatus
                holder.statusIndicator.backgroundTintList =
                    context.resources.getColorStateList(R.color.color_green_done)
            }
            "Cancel" -> {
                holder.statusIndicator.text = item.ttStatus
                holder.statusIndicator.backgroundTintList =
                    context.resources.getColorStateList(R.color.color_bg_yellow)
            }
            "Close" -> {
                holder.statusIndicator.text = item.ttStatus
                holder.statusIndicator.setTextColor(context.resources.getColorStateList(R.color.color_grey))
                holder.statusIndicator.backgroundTintList =
                    context.resources.getColorStateList(R.color.color_closed)
            }
        }

        holder.tvRiskGroup1.text = item.ttGroupLevel1
        holder.tvRiskGroup2.text = item.ttGroupLevel2
        if (item.siteId == null) {
            holder.tvSiteId.text = "Site ID: -"
        } else {
            holder.tvSiteId.text = item.siteId
        }
        holder.tvRegion.text = item.region
        holder.tvRootCause.text = item.rootCauseDesc

        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, TrackerRiskDetailActivity::class.java)
            intent.putExtra("updateGH", "yes")
            intent.putExtra("riskID", item.rttNum)
            intent.putExtra("riskGroupLv1", item.ttGroupLevel1)
            intent.putExtra("riskGroupLv2", item.ttGroupLevel2)
            intent.putExtra("riskRegion", item.region)
            intent.putExtra("riskSiteID", item.siteId)
            intent.putExtra("riskUserComp", item.userComplaint)
            intent.putExtra("riskPic", item.pic)
            intent.putExtra("riskRootCause", item.rootCauseDesc)
            intent.putExtra("riskCreatedOn", item.createdOn)
            intent.putExtra("riskCreatedBy", item.createdBy)
            intent.putExtra("riskUpdateBy", item.updatedBy)
            intent.putExtra("riskUpdateOn", item.updatedOn)
            intent.putExtra("riskStatus", item.ttStatus)
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
        val tvRiskGroup1: TextView = ItemView.tvItemOneStart
        val tvRiskGroup2: TextView = ItemView.tvItemOneEnd
        val tvSiteId: TextView = ItemView.tvItemTwo
        val tvRegion: TextView = ItemView.tvItemThree
        val tvRootCause: TextView = ItemView.tvItemFour
        val imgPriority: ImageView = ItemView.imgPriority
        val statusIndicator: TextView = ItemView.indicatorOne
        val indicatorTwo: TextView = ItemView.indicatorTwo
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<TrackerRiskGHListResponse.CvRegrtrackerVal>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
