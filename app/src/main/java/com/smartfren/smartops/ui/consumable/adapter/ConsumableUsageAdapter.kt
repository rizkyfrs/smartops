package com.smartfren.smartops.ui.consumable.adapter

import android.content.Context
import android.content.Intent
import android.view.Gravity
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
import com.smartfren.smartops.data.models.ConsumableUsageReportResponse
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.data.models.SiteManagementInfoResponse
import com.smartfren.smartops.ui.consumable.ConsumableUsageDetailActivity
import com.smartfren.smartops.ui.dailyactivity.ScheduleViewTaskDetailActivity
import com.smartfren.smartops.ui.sitemanagementinfo.SiteManagementInfoDetailActivity
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.parseDateFromBackend
import com.smartfren.smartops.utils.show
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ConsumableUsageAdapter(private val context: Context) :
    RecyclerView.Adapter<ConsumableUsageAdapter.ViewHolder>() {
    private val mList: ArrayList<ConsumableUsageReportResponse.CvConsflmreport> = ArrayList()

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
        holder.itemLayoutIndicator.hide()

        holder.tvSite.text = item.cflmSiteId
        holder.tvUsageDate.text = item.cflmSiteVisitDate
        holder.tvConsumableName.text = item.cflmConsType
        holder.tvReportStock.text = item.cflmReportType

        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, ConsumableUsageDetailActivity::class.java)
            intent.putExtra("consumableReportID", item.cflmUsageNum)
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
        val tvSite: TextView = ItemView.tvItemOneStart
        val tvUsageDate: TextView = ItemView.tvItemOneEnd
        val tvConsumableName: TextView = ItemView.tvItemTwo
        val tvReportStock: TextView = ItemView.tvItemThree
        val imgPriority: ImageView = ItemView.imgPriority
        val itemLayoutIndicator: LinearLayoutCompat = ItemView.layoutIndicator
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<ConsumableUsageReportResponse.CvConsflmreport>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
