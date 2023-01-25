package com.smartfren.smartops.ui.corefacility.nfmtiket.adapter

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
import com.smartfren.smartops.data.models.NFMTiketResponse
import com.smartfren.smartops.ui.corefacility.nfmtiket.NFMTicketDetailActivity
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.invisible
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class NFMTicketActivityAdapter(private val context: Context) :
    RecyclerView.Adapter<NFMTicketActivityAdapter.ViewHolder>() {
    private val mList: ArrayList<NFMTiketResponse.Ticket> = ArrayList()

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
        holder.tvItemHide.invisible()
        holder.tvActNum.text = item.nFMticketID
//        holder.tvActTitleName.text = "Activity Value"
        holder.tvActName.text = item.nFMAlarmValue
//        holder.tvActTitleLocation.text = "Activity Location"
        holder.tvActLocation.text = item.nFMAlarmValue?.take(10)
        when (item.nFMTicketACKStatus) {
            "ACK" -> {
                holder.imgPriority.setBackgroundResource(R.drawable.bg_done)
            }
            "NOT ACK YET" -> {
                holder.imgPriority.setBackgroundResource(R.drawable.bg_done)
                holder.imgPriority.backgroundTintList = context.resources.getColorStateList(R.color.color_p1)
            }
        }
        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, NFMTicketDetailActivity::class.java)
            intent.putExtra("ticketNum", item.nFMticketNum)
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
//        val tvActTitleNum: TextView = ItemView.tvTitleActName
        val tvActNum: TextView = ItemView.tvItemOneStart
        val tvItemHide: TextView = ItemView.tvItemOneEnd
//        val tvActTitleName: TextView = ItemView.tvTitleSiteID
        val tvActName: TextView = ItemView.tvItemTwo
//        val tvActTitleLocation: TextView = ItemView.tvTitleDateActivityReport
        val tvActLocation: TextView = ItemView.tvItemThree
        val imgPriority: ImageView = ItemView.imgPriority
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<NFMTiketResponse.Ticket>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
