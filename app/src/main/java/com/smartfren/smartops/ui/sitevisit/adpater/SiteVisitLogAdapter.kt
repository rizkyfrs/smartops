package com.smartfren.smartops.ui.sitevisit.adpater

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
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.data.models.SiteManagementInfoResponse
import com.smartfren.smartops.ui.dailyactivity.ScheduleViewTaskDetailActivity
import com.smartfren.smartops.ui.sitemanagementinfo.SiteManagementInfoDetailActivity
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.parseDateFromBackend
import com.smartfren.smartops.utils.show
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SiteVisitLogAdapter(private val context: Context) :
    RecyclerView.Adapter<SiteVisitLogAdapter.ViewHolder>() {
    private val mList: ArrayList<ItemTable> = ArrayList()

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
        holder.tvSiteTech.show()
        holder.tvOMReg.show()
        holder.tvMorphology.show()
        holder.tvSiteStatus.gravity = Gravity.RIGHT
        holder.tvMorphology.gravity = Gravity.RIGHT

        holder.tvSite.text = item.typeOne
        holder.tvSiteStatus.text = item.typeTwo
        holder.tvSiteVendor.text = item.typeThree
        if (item.typeFour != null) holder.tvSalesRegion.text = item.typeFour
        holder.tvSiteTech.text = "Check In\n${item.typeFive}"
        holder.tvOMReg.text = "Check Out\n${item.typeSix}"
        holder.tvMorphology.text =  "Duration\n${item.typeSeven}"

        holder.itemLayout.setOnClickListener {
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
        val tvSiteStatus: TextView = ItemView.tvItemOneEnd
        val tvSiteVendor: TextView = ItemView.tvItemTwo
        val tvSalesRegion: TextView = ItemView.tvItemThree
        val tvSiteTech: TextView = ItemView.tvItemFour
        val tvOMReg: TextView = ItemView.tvItemFive
        val tvMorphology: TextView = ItemView.tvItemFiveEnd
        val imgPriority: ImageView = ItemView.imgPriority
        val itemLayoutIndicator: LinearLayoutCompat = ItemView.layoutIndicator
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
