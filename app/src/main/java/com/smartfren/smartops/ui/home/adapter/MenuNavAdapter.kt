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
import com.smartfren.smartops.ui.testsignal.TestSignalActivity
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.changemanagement.MenuChangeManagementActivity
import com.smartfren.smartops.ui.corefacility.MenuCoreFacilityActivity
import com.smartfren.smartops.ui.dashboard.MenuDashboardActivity
import com.smartfren.smartops.ui.datahub.MenuDataHubActivity
import com.smartfren.smartops.ui.faultmanagement.MenuFaultManagementActivity
import com.smartfren.smartops.ui.fibermanagement.MenuFiberManagementActivity
import com.smartfren.smartops.ui.gis.CustomMarkerClusteringDemoActivity
import com.smartfren.smartops.ui.home.ChangePasswordActivity
import com.smartfren.smartops.ui.materialmanagement.MenuMaterialManagementActivity
import com.smartfren.smartops.ui.powermanagement.MenuPowerManagementActivity
import com.smartfren.smartops.ui.reportingmanagement.MenuReportingManagementActivity
import com.smartfren.smartops.ui.resourcemanagement.MenuResourceManagementActivity
import com.smartfren.smartops.ui.supportmanagement.MenuSupportManagementActivity
import com.smartfren.smartops.ui.taskmanagement.MenuTaskManagementActivity
import com.smartfren.smartops.ui.towermanagement.MenuTowerManagementActivity
import com.smartfren.smartops.ui.workordermanagement.MenuWorkorderManagementActivity
import com.smartfren.smartops.utils.hide
import kotlinx.android.synthetic.main.item_submenu.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuNavAdapter(private val context: Context) :
    RecyclerView.Adapter<MenuNavAdapter.ViewHolder>() {
    private val mList: ArrayList<ContentMenu> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_submenu, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        // sets the text to the textview from our itemHolder class
//        if (position == mList.lastIndex) {
        holder.vLine.hide()
//        }

        holder.tvMenu.setTextColor(context.resources.getColor(R.color.white))
        holder.tvMenu.text = item.name
        holder.imgMenu.setImageResource(item.image)
        holder.imgMenu.imageTintList = context.resources.getColorStateList(R.color.white)
        holder.ivNavItemSubmenu.imageTintList = context.resources.getColorStateList(R.color.white)

        holder.itemLayout.setOnClickListener {
            when (item.id) {
                1 -> {
                    val intent = Intent(context, CustomMarkerClusteringDemoActivity::class.java)
                    context.startActivity(intent)
                }
                17 -> {
                    val intent = Intent(context, TestSignalActivity::class.java)
                    context.startActivity(intent)
                }
                2 -> {
                    val intent = Intent(context, MenuDashboardActivity::class.java)
                    context.startActivity(intent)
                }
                3 -> {
                    val intent = Intent(context, MenuDataHubActivity::class.java)
                    context.startActivity(intent)
                }
                4 -> {
                    val intent = Intent(context, MenuChangeManagementActivity::class.java)
                    context.startActivity(intent)
                }
                5 -> {
                    val intent = Intent(context, MenuCoreFacilityActivity::class.java)
                    context.startActivity(intent)
                }
                6 -> {
                    val intent = Intent(context, MenuFaultManagementActivity::class.java)
                    context.startActivity(intent)
                }
                7 -> {
                    val intent = Intent(context, MenuFiberManagementActivity::class.java)
                    context.startActivity(intent)
                }
                8 -> {
                    val intent = Intent(context, MenuMaterialManagementActivity::class.java)
                    context.startActivity(intent)
                }
                9 -> {
                    val intent = Intent(context, MenuReportingManagementActivity::class.java)
                    context.startActivity(intent)
                }
                10 -> {
                    val intent = Intent(context, MenuPowerManagementActivity::class.java)
                    context.startActivity(intent)
                }
                11 -> {
                    val intent = Intent(context, MenuResourceManagementActivity::class.java)
                    context.startActivity(intent)
                }
                12 -> {
                    val intent = Intent(context, MenuSupportManagementActivity::class.java)
                    context.startActivity(intent)
                }
                13 -> {
                    val intent = Intent(context, MenuTowerManagementActivity::class.java)
                    context.startActivity(intent)
                }
                14 -> {
                    val intent = Intent(context, MenuTaskManagementActivity::class.java)
                    context.startActivity(intent)
                }
                15 -> {
                    val intent = Intent(context, MenuWorkorderManagementActivity::class.java)
                    context.startActivity(intent)
                }
                16 -> {
                    val intent = Intent(context, ChangePasswordActivity::class.java)
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
        val ivNavItemSubmenu: ImageView = ItemView.ivNavItemSubmenu
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