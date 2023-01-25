package com.smartfren.smartops.ui.stom.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.stom.STOMPerformActivity
import com.smartfren.smartops.ui.stom.STOMRenewalActivity
import com.smartfren.smartops.ui.tracker.risk.TrackerRiskActivity
import com.smartfren.smartops.utils.hide
import kotlinx.android.synthetic.main.item_submenu.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuSTOMAdapter(private val context: Context) :
    RecyclerView.Adapter<MenuSTOMAdapter.ViewHolder>() {
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
                    val intent = Intent(context, STOMPerformActivity::class.java)
                    context.startActivity(intent)
                }
                2 -> {
                    val intent = Intent(context, STOMRenewalActivity::class.java)
                    context.startActivity(intent)
                }
                3 -> {
//                    val intent = Intent(context, WIPActivity::class.java)
//                    context.startActivity(intent)
                }
                4 -> {
                    if(MainApp.instance.sharedPreferences?.getString("levelID", "") == "501") {
                        val intent = Intent(context, TrackerRiskActivity::class.java)
                        context.startActivity(intent)
                    }
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