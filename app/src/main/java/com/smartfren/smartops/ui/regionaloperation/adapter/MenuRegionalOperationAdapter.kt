package com.smartfren.smartops.ui.regionaloperation.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.ccf.CCFActivity
import com.smartfren.smartops.ui.consumable.ConsumableUsageActivity
import com.smartfren.smartops.ui.corefacility.ccfreport.CCFReportActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivity
import com.smartfren.smartops.ui.history.MyHistoryActivity
import com.smartfren.smartops.ui.liveflm.LiveFLMActivity
import com.smartfren.smartops.ui.materialdeliveryrequest.MDRActivity
import com.smartfren.smartops.ui.nos.MenuNOSActivity
import com.smartfren.smartops.ui.regionaloperation.MenuRegionalOperationActivity
import com.smartfren.smartops.ui.regionalusermanager.RegionalUserManagerActivity
import com.smartfren.smartops.ui.reportactivity.ActivityReportActivity
import com.smartfren.smartops.ui.sitemanagementinfo.SiteManagementInfoActivity
import com.smartfren.smartops.ui.sitevisit.SiteVisitLogActivity
import com.smartfren.smartops.ui.stolen.StolenReportActivity
import com.smartfren.smartops.ui.stom.MenuSTOMActivity
import com.smartfren.smartops.ui.stom.STOMPerformActivity
import com.smartfren.smartops.ui.stom.STOMRenewalActivity
import com.smartfren.smartops.ui.tracker.risk.TrackerRiskActivity
import com.smartfren.smartops.ui.ttwo.TTWOActivity
import com.smartfren.smartops.ui.workordermanagement.WorkorderActivity
import com.smartfren.smartops.utils.hide
import kotlinx.android.synthetic.main.item_submenu.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuRegionalOperationAdapter(private val context: Context) :
    RecyclerView.Adapter<MenuRegionalOperationAdapter.ViewHolder>() {
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
        holder.itemLayout.setOnClickListener {
            when (item.id) {
                1 -> {
                    val intent = Intent(context, DailyActivity::class.java)
                    context.startActivity(intent)
                }
                2 -> {
                    val intent = Intent(context, ActivityReportActivity::class.java)
                    context.startActivity(intent)
                }
                3 -> {
                    val intent = Intent(context, MyHistoryActivity::class.java)
                    context.startActivity(intent)
                }
                4 -> {
//                    val intent = Intent(context, TrackerRiskActivity::class.java)
//                    context.startActivity(intent)
                }
                5 -> {
                    val intent = Intent(context, SiteVisitLogActivity::class.java)
                    context.startActivity(intent)
                }
                6 -> {
                    val intent = Intent(context, ConsumableUsageActivity::class.java)
                    context.startActivity(intent)
                }
                7 -> {
                    val intent = Intent(context, WorkorderActivity::class.java)
                    context.startActivity(intent)
                }
                8 -> {
                    val intent = Intent(context, TrackerRiskActivity::class.java)
                    context.startActivity(intent)
                }
                9 -> {
                    val intent = Intent(context, RegionalUserManagerActivity::class.java)
                    context.startActivity(intent)
                }
                10 -> {
                    val intent = Intent(context, LiveFLMActivity::class.java)
                    context.startActivity(intent)
                }
                11 -> {
                    val intent = Intent(context, MenuSTOMActivity::class.java)
                    context.startActivity(intent)
                }
                12 -> {
                    val intent = Intent(context, StolenReportActivity::class.java)
                    context.startActivity(intent)
                }
                13 -> {
                    val intent = Intent(context, MDRActivity::class.java)
                    context.startActivity(intent)
                }
                21 -> {
                    val intent = Intent(context, MenuRegionalOperationActivity::class.java)
                    context.startActivity(intent)
                }
                22 -> {
                    val intent = Intent(context, MenuSTOMActivity::class.java)
                    context.startActivity(intent)
                }
                23 -> {
//                    val intent = Intent(context, MenuNOSActivity::class.java)
//                    context.startActivity(intent)
                    showDialogFeature("Feature Not Available","This feature not available for now.")
                }
                24 -> {
                    val intent = Intent(context, CCFActivity::class.java)
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

    private fun showDialogFeature(titles: String?, descs: String?) {
        val dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_success)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val btnOk = dialog.findViewById(R.id.btnOk) as Button
        val imgView = dialog.findViewById(R.id.imgSuccess) as ImageView
        val title = dialog.findViewById(R.id.tvTitleSuccess) as TextView
        val desc = dialog.findViewById(R.id.tvSuccess) as TextView

        imgView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_error))
        imgView.imageTintList = context.resources.getColorStateList(R.color.color_pink)
        if (titles != null && titles != "") title.text = titles
        if (descs != null && descs != "") desc.text = descs

        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}