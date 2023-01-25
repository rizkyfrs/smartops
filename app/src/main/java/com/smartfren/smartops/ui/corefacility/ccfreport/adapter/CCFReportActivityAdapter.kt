package com.smartfren.smartops.ui.corefacility.ccfreport.adapter

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
import com.smartfren.smartops.data.models.CCFReportResponse
import com.smartfren.smartops.ui.corefacility.ccfreport.CCFReportDetailActivity
import com.smartfren.smartops.utils.show
import kotlinx.android.synthetic.main.item_activity_report.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CCFReportActivityAdapter(private val context: Context) :
    RecyclerView.Adapter<CCFReportActivityAdapter.ViewHolder>() {
    private val mList: ArrayList<CCFReportResponse.Activity> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity_report, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.tvActTitleNum.text = "Activity Num"
        holder.tvActNum.text = item.activityID
        holder.tvActTitleName.text = "Activity Name"
        holder.tvActName.text = item.activityCategory
        holder.tvActTitleLocation.text = "Activity Location"
        holder.tvActLocation.text = item.activityLocation
        when (item.validationCCF) {
            "val" -> {
                holder.imgPriority.setBackgroundResource(R.drawable.bg_done)
            }
            "inv" -> {
                holder.imgPriority.setBackgroundResource(R.drawable.bg_p1)
            }
        }
        holder.imgValid.show()
        when (item.validationSME) {
            "val" -> {
                holder.imgValid.setBackgroundResource(R.drawable.bg_done)
            }
            "inv" -> {
                holder.imgValid.setBackgroundResource(R.drawable.bg_p1)
            }
        }
        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, CCFReportDetailActivity::class.java)
            intent.putExtra("report_id", item.activityNum)
            context.startActivity(intent)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val itemLayout: CardView = ItemView.cvActivityReport
        val tvActTitleNum: TextView = ItemView.tvTitleActName
        val tvActNum: TextView = ItemView.tvActName
        val tvActTitleName: TextView = ItemView.tvTitleSiteID
        val tvActName: TextView = ItemView.tvSiteID
        val tvActTitleLocation: TextView = ItemView.tvTitleDateActivityReport
        val tvActLocation: TextView = ItemView.tvDateActivityReport
        val imgPriority: ImageView = ItemView.imgPriority
        val imgValid: ImageView = ItemView.imgValid
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<CCFReportResponse.Activity>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
