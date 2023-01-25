package com.smartfren.smartops.ui.tracker.adapter

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
import com.smartfren.smartops.ui.tracker.ac.TrackerACActivity
import com.smartfren.smartops.ui.tracker.accessibility.TrackerAccessibilityActivity
import com.smartfren.smartops.ui.tracker.battery.TrackerBatteryActivity
import com.smartfren.smartops.ui.tracker.fgs.TrackerFGSActivity
import com.smartfren.smartops.ui.tracker.grounding.TrackerGroundingActivity
import com.smartfren.smartops.ui.tracker.imb.TrackerIMBActivity
import com.smartfren.smartops.ui.tracker.landscape.TrackerLandscapeActivity
import com.smartfren.smartops.ui.tracker.nweeks.TrackerNWeeksActivity
import com.smartfren.smartops.ui.tracker.problematic.TrackerProblematicActivity
import com.smartfren.smartops.ui.tracker.travelling.TrackerTravellingActivity
import com.smartfren.smartops.ui.tracker.visibility.TrackerVisibilityActivity
import com.smartfren.smartops.ui.tracker.worstcell.TrackerWortCellActivity
import com.smartfren.smartops.utils.hide
import kotlinx.android.synthetic.main.item_submenu.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuTrackerAdapter(private val context: Context) :
    RecyclerView.Adapter<MenuTrackerAdapter.ViewHolder>() {
    private val mList: ArrayList<ContentMenu> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_submenu, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        // sets the text to the textview from our itemHolder class
        if (position == mList.lastIndex) {
            holder.vLine.hide()
        }
        holder.tvMenu.text = item.name
        holder.imgMenu.setImageResource(item.image)
        holder.imgMenu.imageTintList = context.resources.getColorStateList(R.color.white)
        holder.itemLayout.setOnClickListener {
            when (item.id) {
                1 -> {
                    val intent = Intent(context, TrackerFGSActivity::class.java)
                    context.startActivity(intent)
                }
                2 -> {
                    val intent = Intent(context, TrackerBatteryActivity::class.java)
                    context.startActivity(intent)
                }
                3 -> {
                    val intent = Intent(context, TrackerACActivity::class.java)
                    context.startActivity(intent)
                }
                4 -> {
                    val intent = Intent(context, TrackerGroundingActivity::class.java)
                    context.startActivity(intent)
                }
                5 -> {
                    val intent = Intent(context, TrackerLandscapeActivity::class.java)
                    context.startActivity(intent)
                }
                6 -> {
                    val intent = Intent(context, TrackerAccessibilityActivity::class.java)
                    context.startActivity(intent)
                }
                7 -> {
                    val intent = Intent(context, TrackerTravellingActivity::class.java)
                    context.startActivity(intent)
                }
                8 -> {
                    val intent = Intent(context, TrackerIMBActivity::class.java)
                    context.startActivity(intent)
                }
                9 -> {
                    val intent = Intent(context, TrackerVisibilityActivity::class.java)
                    context.startActivity(intent)
                }
                10 -> {
                    val intent = Intent(context, TrackerProblematicActivity::class.java)
                    context.startActivity(intent)
                }
                11 -> {
                    val intent = Intent(context, TrackerWortCellActivity::class.java)
                    context.startActivity(intent)
                }
                12 -> {
                    val intent = Intent(context, TrackerNWeeksActivity::class.java)
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
        val tvMenu: TextView = ItemView.tvItemSubmenu
        val imgMenu: ImageView = ItemView.ivItemSubmenu
        val vLine: View = ItemView.lineOne
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
