package com.smartfren.smartops.ui.reportactivity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ActivityChecklistResponse
import com.smartfren.smartops.ui.reportactivity.ActivityChecklistUpdateActivity
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import kotlinx.android.synthetic.main.item_activity_report.view.*

class ActivityChecklistAdapter(private val context: Context) :
    RecyclerView.Adapter<ActivityChecklistAdapter.ViewHolder>() {
    private val mList: ArrayList<ActivityChecklistResponse.Check> = ArrayList()

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
        holder.trOne.hide()
        holder.trTwo.hide()
        holder.trThree.hide()
        holder.tvNoDetail.show()
        holder.tvDetail.show()
        holder.tvNoDetail.text = item.checkListSeqNum + ". "
        holder.tvDetail.text = item.checkListItem
        when (item.checkListItemStatus) {
            "0" -> {
                holder.imgPriority.setBackgroundResource(R.drawable.bg_close)
                holder.imgPriority.backgroundTintList =
                    context.resources.getColorStateList(R.color.color_red_down)
            }
            "1" -> {
                holder.imgPriority.setBackgroundResource(R.drawable.bg_done)
            }
            "2" -> {
                holder.imgPriority.setBackgroundResource(R.drawable.bg_p_uncategory)
            }
        }
        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, ActivityChecklistUpdateActivity::class.java)
            intent.putExtra("checklistItem", "${item.checkListSeqNum + ". " + item.checkListItem}")
            intent.putExtra("checklistID", item.checkListID)
            intent.putExtra("activityResultNum", item.activityResultNum)
            intent.putExtra("actResultOpt", item.resultOption)
            intent.putExtra("actResultText", item.resultText)
            intent.putExtra("actResultAtt", item.resultAtt)
            intent.putExtra("actItemStatus", item.checkListItemStatus)
            intent.putExtra("resultOptionMandatory", item.resultOptionMandatory)
            intent.putExtra("resultTextMandatory", item.resultTextMandatory)
            intent.putExtra("resultAttachmentMandatory", item.resultAttachmentMandatory)
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
        val trOne: TableRow = ItemView.trOne
        val trTwo: TableRow = ItemView.trTwo
        val trThree: TableRow = ItemView.trThree
        val tvNoDetail: TextView = ItemView.tvNoDetail
        val tvDetail: TextView = ItemView.tvDetail
        val imgPriority: ImageView = ItemView.imgPriority
    }

    fun updateList(lstItem: List<ActivityChecklistResponse.Check>) {
        mList.clear()
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
