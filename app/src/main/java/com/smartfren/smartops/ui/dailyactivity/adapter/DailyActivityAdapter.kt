package com.smartfren.smartops.ui.dailyactivity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.dailyactivity.DateItem
import com.smartfren.smartops.ui.dailyactivity.GeneralItem
import com.smartfren.smartops.ui.dailyactivity.ListItem
import com.smartfren.smartops.ui.dailyactivity.ScheduleTaskListDetailActivity
import com.smartfren.smartops.utils.defaultFormat
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.parseDateFull
import com.smartfren.smartops.utils.show
import kotlinx.android.synthetic.main.item_schedule_title.view.*
import kotlinx.android.synthetic.main.item_task.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DailyActivityAdapter(context: Context, consolidatedList: List<ListItem>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val mContext: Context
    var consolidatedList: List<ListItem>? = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ListItem.TYPE_GENERAL -> {
                val v1: View = inflater.inflate(
                    R.layout.item_task, parent,
                    false
                )
                viewHolder = GeneralViewHolder(v1)
            }
            ListItem.TYPE_DATE -> {
                val v2: View = inflater.inflate(R.layout.item_schedule_title, parent, false)
                viewHolder = DateViewHolder(v2)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            ListItem.TYPE_GENERAL -> {
                val generalItem = consolidatedList?.get(position) as GeneralItem
                val generalViewHolder = holder as GeneralViewHolder?
                generalViewHolder?.tvSite?.text = generalItem.pojoOfJsonArray?.name
                generalViewHolder?.tvDate?.text = generalItem.pojoOfJsonArray?.date
                if (MainApp.instance.sharedPreferences?.getString("levelID", "") != "35") {
                    generalViewHolder?.tvPICName?.show()
                    generalViewHolder?.tvPICName?.text = generalItem.pojoOfJsonArray?.PICName
                }
                generalViewHolder?.tvP0Task?.text = generalItem.pojoOfJsonArray?.p0
                generalViewHolder?.tvP1Task?.text = generalItem.pojoOfJsonArray?.p1
                generalViewHolder?.tvP2Task?.text = generalItem.pojoOfJsonArray?.p2
                generalViewHolder?.progressSite?.progress =
                    generalItem.pojoOfJsonArray?.progress?.toDouble()!!.toInt()
                generalViewHolder?.tvProgress?.text =
                    "${defaultFormat(generalItem.pojoOfJsonArray?.progress?.toDouble())}%"
                generalViewHolder?.itemLayout?.setOnClickListener {
                    val intent = Intent(mContext, ScheduleTaskListDetailActivity::class.java)
                    intent.putExtra("site", generalItem.pojoOfJsonArray?.name)
                    mContext.startActivity(intent)
                }
            }
            ListItem.TYPE_DATE -> {
                val dateItem = consolidatedList?.get(position) as DateItem
                val dateViewHolder = holder as DateViewHolder?
                if (dateItem.date != null) {
                    dateViewHolder?.txtTitle?.text = parseDateFull(dateItem.date, "dd-MM-yyyy")
                } else {
                    dateViewHolder?.txtTitle?.hide()
                }
            }
        }
    }

    // ViewHolder for date row item
    internal inner class DateViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var txtTitle: TextView = v.tvTitleDateSchedule

    }

    // View holder for general row item
    internal inner class GeneralViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvSite: TextView = v.tvTaskSiteId
        val tvDate: TextView = v.tvTaskDate
        val tvPICName: TextView = v.tvTaskPIC
        val tvP0Task: TextView = v.tvP0Task
        val tvP1Task: TextView = v.tvP1Task
        val tvP2Task: TextView = v.tvP2Task
        val itemLayout: CardView = v.itemLayoutSchedule
        val progressSite: ProgressBar = v.progressSite
        val tvProgress: TextView = v.tvProgressTask
    }

    override fun getItemViewType(position: Int): Int {
        return consolidatedList?.get(position)!!.type
    }

    override fun getItemCount(): Int {
        return if (consolidatedList != null) consolidatedList!!.size else 0
    }

    init {
        this.consolidatedList = consolidatedList
        mContext = context
    }
}