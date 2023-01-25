package com.smartfren.smartops.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.corefacility.ccfreport.CCFReportActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivity
import com.smartfren.smartops.ui.history.MyHistoryActivity
import com.smartfren.smartops.ui.corefacility.nfmtiket.NFMTicketActivity
import com.smartfren.smartops.ui.reportactivity.ActivityReportActivity
import kotlinx.android.synthetic.main.content_menu_item_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuAdapter(private val context: Context) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    private val mList: ArrayList<ContentMenu> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_menu_item_activity, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.tvMenu.text = item.name
        holder.imgMenu.setImageResource(item.image)
        holder.itemLayout.setOnClickListener {
            when (item.id) {
                1 -> {
                    val intent = Intent(context, DailyActivity::class.java)
                    context.startActivity(intent)
                }
                2 -> {
                    val intent = Intent(context, MyHistoryActivity::class.java)
                    context.startActivity(intent)
                }
                3 -> {
                    val intent = Intent(context, CCFReportActivity::class.java)
                    context.startActivity(intent)
                }
                4 -> {
                    val intent = Intent(context, NFMTicketActivity::class.java)
                    context.startActivity(intent)
                }
                5 -> {
                    val intent = Intent(context, ActivityReportActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val itemLayout: ConstraintLayout = ItemView.itemLayout
        val tvMenu: TextView = ItemView.txtMenu
        val imgMenu: ImageView = ItemView.imgMenu
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<ContentMenu>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
