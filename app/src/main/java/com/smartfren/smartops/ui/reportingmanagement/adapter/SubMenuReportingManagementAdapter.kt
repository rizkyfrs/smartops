package com.smartfren.smartops.ui.reportingmanagement.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.entity.SiteVisit
import com.smartfren.smartops.ui.consumable.ConsumableUsageActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivity
import com.smartfren.smartops.ui.materialdeliveryrequest.MDRActivity
import com.smartfren.smartops.ui.reportactivity.ActivityReportActivity
import com.smartfren.smartops.ui.sitemanagementinfo.SiteManagementInfoActivity
import com.smartfren.smartops.ui.sitevisit.SiteVisitLogActivity
import com.smartfren.smartops.ui.tracker.risk.TrackerRiskActivity
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.invisible
import com.smartfren.smartops.utils.show

class SubMenuReportingManagementAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val itemList: ArrayList<Item> = ArrayList()
    private var currentOpenedParent: Parent? = null

    override fun getItemCount() = itemList.size

    override fun getItemViewType(position: Int): Int {
        return itemList[position].getItemType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            CHILD -> {
                val v1: View = inflater.inflate(
                    R.layout.item_submenu_top, parent,
                    false
                )
                viewHolder = ChildViewHolder(v1)
            }
            else -> {
                val v2: View = inflater.inflate(
                    R.layout.item_submenu_top, parent,
                    false
                )
                viewHolder = ParentViewHolder(v2)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            CHILD -> {
                val childViewHolder = (holder as ChildViewHolder)
                childViewHolder.childItem = itemList[position] as Child
                childViewHolder.bind()
//                if (position == itemList.lastIndex) {
//                    childViewHolder.updateHideLineLast()
//                }
            }
            else -> {
                val parentViewHolder = holder as ParentViewHolder
                parentViewHolder.parentItem = itemList[position] as Parent
                parentViewHolder.bind()
                if (position == 0) {
                    parentViewHolder.updateHideLineLast()
                } else {
                    parentViewHolder.updateShowLineLast()
                }
            }
        }
    }

    inner class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val startPosition = adapterPosition + 1
                val count = parentItem.childItems.size

                if (parentItem.isExpanded) {
                    itemList.removeAll(parentItem.childItems.toSet())
                    notifyItemRangeRemoved(startPosition, count)
                    parentItem.isExpanded = false
                    currentOpenedParent = null
                } else {
                    itemList.addAll(startPosition, parentItem.childItems)
                    notifyItemRangeInserted(startPosition, count)
                    parentItem.isExpanded = true

                    if (currentOpenedParent != null) {
                        itemList.removeAll(currentOpenedParent?.childItems!!.toSet())
                        notifyItemRangeRemoved(
                            itemList.indexOf(currentOpenedParent!!) + 1,
                            currentOpenedParent!!.childItems.size
                        )
                        currentOpenedParent?.isExpanded = false
                        notifyItemChanged(itemList.indexOf(currentOpenedParent!!))
                    }

                    currentOpenedParent = parentItem
                }
                updateViewState()

                when (parentItem.id) {
                    1 -> {

                    }
                }
            }
        }

        lateinit var parentItem: Parent

        private val title: TextView = itemView.findViewById(R.id.tvItemSubmenu)
        private val ivNavItemSubmenu: ImageView = itemView.findViewById(R.id.ivNavItemSubmenu)
        private val lineOne: View = itemView.findViewById(R.id.lineOne)

        fun bind() {
            title.text = parentItem.title
            updateViewState()
        }

        fun updateHideLineLast() {
            lineOne.hide()
        }

        private fun updateViewState() {
            if (parentItem.selectedChild != null) {
                title.text = parentItem.selectedChild?.title
                return
            }
            if (parentItem.isExpanded) {
                if (parentItem.childItems.size != 0)
                ivNavItemSubmenu.rotation = 90F
            } else {
                ivNavItemSubmenu.rotation = 0F
            }
        }

        fun updateShowLineLast() {
            lineOne.show()
        }
    }

    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
//                val parentPosition = itemList.indexOf(childItem.parent)
//                val startPosition = parentPosition + 1
//                val count = childItem.parent.childItems.size
//
//                itemList.removeAll(childItem.parent.childItems)
//                notifyItemRangeRemoved(startPosition, count)
//                childItem.parent.isExpanded = false
//
//                childItem.parent.selectedChild = childItem
//
//                notifyItemChanged(parentPosition)

                when (childItem.id) {
                    1 -> {

                    }
                    2 -> {

                    }
                    3 -> {

                    }
                }
            }
        }

        lateinit var childItem: Child

        private val title: TextView = itemView.findViewById(R.id.tvItemSubmenu)
        private val lineOne: View = itemView.findViewById(R.id.lineOne)
        private val ivItemSubmenu: View = itemView.findViewById(R.id.ivItemSubmenu)

        fun bind() {
            title.text = childItem.title
            if (childItem.image == 0) {
                ivItemSubmenu.invisible()
            }
        }

        fun updateHideLineLast() {
            lineOne.hide()
        }
    }

    fun updateList(lstItem: List<Item>) {
        itemList.addAll(lstItem)
        notifyDataSetChanged()
    }
}
