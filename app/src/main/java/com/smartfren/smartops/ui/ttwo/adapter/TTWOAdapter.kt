package com.smartfren.smartops.ui.ttwo.adapter

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
import com.smartfren.smartops.data.models.TTWOListResponse
import com.smartfren.smartops.ui.ttwo.TTWODetailActivity
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TTWOAdapter(private val context: Context) :
    RecyclerView.Adapter<TTWOAdapter.ViewHolder>() {
    private val mList: ArrayList<TTWOListResponse.CvFsttwohistRCA> = ArrayList()

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
        holder.tvUsageDate.hide()
        holder.tvItemFour.show()

        holder.tvSite.text = item.neName
        holder.tvConsumableName.text = item.alarmName
        holder.tvReportStock.text = item.timeStart
        holder.tvItemFour.text = item.timeEnd

        if (item.uRca1.isNullOrEmpty() && item.uRca2.isNullOrEmpty() && item.uRca3.isNullOrEmpty()) {
            holder.imgPriority.setBackgroundResource(R.drawable.bg_done)
            holder.imgPriority.backgroundTintList = context.resources.getColorStateList(R.color.color_p1)
        } else {
            holder.imgPriority.setBackgroundResource(R.drawable.bg_done)
        }

        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, TTWODetailActivity::class.java)
            intent.putExtra("ttwoID", "${item.histNum}")
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
        val tvItemFour: TextView = ItemView.tvItemFour
        val imgPriority: ImageView = ItemView.imgPriority
        val itemLayoutIndicator: LinearLayoutCompat = ItemView.layoutIndicator
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<TTWOListResponse.CvFsttwohistRCA>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
