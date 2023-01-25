package com.smartfren.smartops.ui.materialdeliveryrequest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.MDRListResponse
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MDRAdapter(private val context: Context) :
    RecyclerView.Adapter<MDRAdapter.ViewHolder>() {
    private val mList: ArrayList<MDRListResponse.TbStockMdr> = ArrayList()

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

        holder.itemLayoutIndicator.hide()
        holder.tvTipeMdr.show()
        holder.tvItemMdr.text = item.fsmdrSiteId
        holder.tvVendorMdr.text = item.fsmdrId
        holder.tvSiteMdr.text = item.fsmdrWoId
        holder.tvTipeMdr.text = item.fsmdrDop
        if (item.fsmdrDate != null) holder.tvDate.text = item.fsmdrDate
        if (item.fsmdrStatus != null) {
            when (item.fsmdrStatus) {
                "SENT" -> {
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
//            val intent = Intent(context, ScheduleViewTaskDetailActivity::class.java)
//            intent.putExtra("history", "true")
//            intent.putExtra("site", item.fsmdrSiteId)
//            intent.putExtra("activityNumber", item.actNum)
//            context.startActivity(intent)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val itemLayout: CardView = ItemView.itemLayoutSchedule
        val tvItemMdr: TextView = ItemView.tvItemOneStart
        val tvDate: TextView = ItemView.tvItemOneEnd
        val tvVendorMdr: TextView = ItemView.tvItemTwo
        val tvSiteMdr: TextView = ItemView.tvItemThree
        val tvTipeMdr: TextView = ItemView.tvItemFour
        val tvLocationMdr: TextView = ItemView.tvItemFive
        val imgPriority: ImageView = ItemView.imgPriority
        val itemLayoutIndicator: LinearLayoutCompat = ItemView.layoutIndicator
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<MDRListResponse.TbStockMdr>?) {
        lstItem?.let { mList.addAll(it) }
        notifyDataSetChanged()
    }
}
