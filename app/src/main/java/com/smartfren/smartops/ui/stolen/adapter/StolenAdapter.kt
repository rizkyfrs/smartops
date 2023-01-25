package com.smartfren.smartops.ui.stolen.adapter

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
import com.smartfren.smartops.data.models.StolenListReportResponse
import com.smartfren.smartops.ui.stolen.StolenReportDetailActivity
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.setDelimiterComma
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class StolenAdapter(private val context: Context) :
    RecyclerView.Adapter<StolenAdapter.ViewHolder>() {
    private val mList: ArrayList<StolenListReportResponse.CvStolenReport> = ArrayList()

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
        holder.tvItemFour.hide()

        holder.tvSite.text = setDelimiterComma(item.srSiteId)
        holder.tvDateLoss.text = item.srDateOfLoss
        holder.tvExistingSiteSec.text = item.srExistingSecurity
        holder.tvRegion.text = item.srRegion

        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, StolenReportDetailActivity::class.java)
            intent.putExtra("stolenID", "${item.srNum}")
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
        val tvDateLoss: TextView = ItemView.tvItemOneEnd
        val tvExistingSiteSec: TextView = ItemView.tvItemTwo
        val tvRegion: TextView = ItemView.tvItemThree
        val tvItemFour: TextView = ItemView.tvItemFour
        val imgPriority: ImageView = ItemView.imgPriority
        val itemLayoutIndicator: LinearLayoutCompat = ItemView.layoutIndicator
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<StolenListReportResponse.CvStolenReport>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
