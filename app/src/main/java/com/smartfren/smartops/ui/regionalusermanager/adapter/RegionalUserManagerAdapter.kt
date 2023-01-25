package com.smartfren.smartops.ui.regionalusermanager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.RegionalUserManagerListResponse
import com.smartfren.smartops.data.models.TTWOListResponse
import com.smartfren.smartops.ui.regionalusermanager.RegionalUserManagerCallback
import com.smartfren.smartops.ui.regionalusermanager.RegionalUserManagerDetailActivity
import com.smartfren.smartops.ui.ttwo.TTWODetailActivity
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.replacePoint
import com.smartfren.smartops.utils.show
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@SuppressLint("NotifyDataSetChanged")
class RegionalUserManagerAdapter(private val context: Context) :
    RecyclerView.Adapter<RegionalUserManagerAdapter.ViewHolder>() {
    private val mList: ArrayList<RegionalUserManagerListResponse.CvRegusermgm> = ArrayList()

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
        holder.tvItemOneEnd.hide()
        holder.tvItemFour.show()

        holder.tvName.text = item.uName
        if (item.idParrent != null) {
            holder.tvItemTwo.text = item.idParrent
        } else {
            holder.tvItemTwo.text = "-"
        }
        holder.tvItemThree.text = item.uLevel
        holder.tvItemFour.text = item.uCompany
        holder.imgIconItem.setImageDrawable(context.resources.getDrawable(R.drawable.img_account))

        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, RegionalUserManagerDetailActivity::class.java)
            intent.putExtra("regUserMgmID", "${replacePoint(item.idUser)}")
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
        val tvName: TextView = ItemView.tvItemOneStart
        val tvItemOneEnd: TextView = ItemView.tvItemOneEnd
        val tvItemTwo: TextView = ItemView.tvItemTwo
        val tvItemThree: TextView = ItemView.tvItemThree
        val tvItemFour: TextView = ItemView.tvItemFour
        val imgPriority: ImageView = ItemView.imgPriority
        val imgIconItem: ImageView = ItemView.ivIconItem
        val itemLayoutIndicator: LinearLayoutCompat = ItemView.layoutIndicator
    }

//    @SuppressLint("NotifyDataSetChanged")
    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<RegionalUserManagerListResponse.CvRegusermgm>) {
        val diffResult = DiffUtil.calculateDiff(RegionalUserManagerCallback(mList, lstItem))
        mList.addAll(lstItem)
//        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}
