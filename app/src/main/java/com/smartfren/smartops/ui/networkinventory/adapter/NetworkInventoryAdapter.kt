package com.smartfren.smartops.ui.networkinventory.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.utils.hide
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class NetworkInventoryAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mList: ArrayList<ItemTable> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item

        if (viewType == TYPE_CHART) {
            return GfgViewOne(
                LayoutInflater.from(context).inflate(R.layout.item_chart_stack, parent, false)
            )
        } else {
            return View2ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_table_view, parent, false)
            )
        }

//        val layout = when (viewType) {
//            TYPE_CHART -> R.layout.item_chart_stack
//            TYPE_TABLE -> R.layout.item_table_view
//            else -> throw IllegalArgumentException("Invalid view type")
//        }

//        val view = LayoutInflater
//            .from(parent.context)
//            .inflate(layout, parent, false)
//
//        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mList[position]

//        if (position == mList.lastIndex) {
//            holder.vwLine.hide()
//        }
        if (mList[position].textColor == TYPE_CHART) {
            (holder as GfgViewOne).bind(position)
        } else {
            (holder as View2ViewHolder).bind(position)
        }

        // sets the text to the textview from our itemHolder class
//        if (item.textStyle == "bold") {
//            holder.tvItemOne.setTypeface(null, Typeface.BOLD)
//            holder.tvItemTwo.setTypeface(null, Typeface.BOLD)
//            holder.tvItemThree.setTypeface(null, Typeface.BOLD)
//            holder.tvItemFour.setTypeface(null, Typeface.BOLD)
//            holder.tvItemFive.setTypeface(null, Typeface.BOLD)
//            holder.tvItemSix.setTypeface(null, Typeface.BOLD)
//            holder.tvItemSeven.setTypeface(null, Typeface.BOLD)
//            holder.tvItemEight.setTypeface(null, Typeface.BOLD)
//            holder.tvItemNine.setTypeface(null, Typeface.BOLD)
//            holder.tvItemTen.setTypeface(null, Typeface.BOLD)
//        }
//
//        holder.tvItemOne.setTextColor(context.resources.getColor(item.textColor))
//        holder.tvItemTwo.setTextColor(context.resources.getColor(item.textColor))
//        holder.tvItemThree.setTextColor(context.resources.getColor(item.textColor))
//        holder.tvItemFour.setTextColor(context.resources.getColor(item.textColor))
//        holder.tvItemFive.setTextColor(context.resources.getColor(item.textColor))
//        holder.tvItemSix.setTextColor(context.resources.getColor(item.textColor))
//        holder.tvItemSeven.setTextColor(context.resources.getColor(item.textColor))
//        holder.tvItemEight.setTextColor(context.resources.getColor(item.textColor))
//        holder.tvItemNine.setTextColor(context.resources.getColor(item.textColor))
//        holder.tvItemTen.setTextColor(context.resources.getColor(item.textColor))
//
//        holder.tvItemOne.text = item.typeOne
//        holder.tvItemTwo.text = item.typeTwo
//        if (item.typeThree != "") {
//            holder.tvItemThree.text = item.typeThree
//        } else {
//            holder.tvItemThree.hide()
//        }
//        if (item.typeFour != "") {
//            holder.tvItemFour.text = item.typeFour
//        } else {
//            holder.tvItemFour.hide()
//        }
//
//        if (item.typeFive != "") {
//            holder.tvItemFive.text = item.typeFive
//        } else {
//            holder.tvItemFive.hide()
//        }
//
//        if (item.typeSix != "") {
//            holder.tvItemSix.text = item.typeSix
//        } else {
//            holder.tvItemSix.hide()
//        }
//
//        if (item.typeSeven != "") {
//            holder.tvItemSeven.text = item.typeSeven
//        } else {
//            holder.tvItemSeven.hide()
//        }
//
//        if (item.typeEight != "") {
//            holder.tvItemEight.text = item.typeEight
//        } else {
//            holder.tvItemEight.hide()
//        }
//
//        if (item.typeNine != "") {
//            holder.tvItemNine.text = item.typeNine
//        } else {
//            holder.tvItemNine.hide()
//        }
//
//        if (item.typeTen != "") {
//            holder.tvItemTen.text = item.typeTen
//        } else {
//            holder.tvItemTen.hide()
//        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
//    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
//        val tvItemOne: TextView = ItemView.tvItemOne
//        val tvItemTwo: TextView = ItemView.tvItemTwo
//        val tvItemThree: TextView = ItemView.tvItemThree
//        val tvItemFour: TextView = ItemView.tvItemFour
//        val tvItemFive: TextView = ItemView.tvItemFive
//        val tvItemSix: TextView = ItemView.tvItemSix
//        val tvItemSeven: TextView = ItemView.tvItemSeven
//        val tvItemEight: TextView = ItemView.tvItemEight
//        val tvItemNine: TextView = ItemView.tvItemNine
//        val tvItemTen: TextView = ItemView.tvItemTen
//        val vwLine: View = ItemView.vwLine
//
//        private fun bindTable(item: ItemTable) {
//
//            // sets the text to the textview from our itemHolder class
//            if (item.textStyle == "bold") {
//                tvItemOne.setTypeface(null, Typeface.BOLD)
//                tvItemTwo.setTypeface(null, Typeface.BOLD)
//                tvItemThree.setTypeface(null, Typeface.BOLD)
//                tvItemFour.setTypeface(null, Typeface.BOLD)
//                tvItemFive.setTypeface(null, Typeface.BOLD)
//                tvItemSix.setTypeface(null, Typeface.BOLD)
//                tvItemSeven.setTypeface(null, Typeface.BOLD)
//                tvItemEight.setTypeface(null, Typeface.BOLD)
//                tvItemNine.setTypeface(null, Typeface.BOLD)
//                tvItemTen.setTypeface(null, Typeface.BOLD)
//            }
//
//            tvItemOne.text = item.typeOne
//            tvItemTwo.text = item.typeTwo
//            if (item.typeThree != "") {
//                tvItemThree.text = item.typeThree
//            } else {
//                tvItemThree.hide()
//            }
//            if (item.typeFour != "") {
//                tvItemFour.text = item.typeFour
//            } else {
//                tvItemFour.hide()
//            }
//
//            if (item.typeFive != "") {
//                tvItemFive.text = item.typeFive
//            } else {
//                tvItemFive.hide()
//            }
//
//            if (item.typeSix != "") {
//                tvItemSix.text = item.typeSix
//            } else {
//                tvItemSix.hide()
//            }
//
//            if (item.typeSeven != "") {
//                tvItemSeven.text = item.typeSeven
//            } else {
//                tvItemSeven.hide()
//            }
//
//            if (item.typeEight != "") {
//                tvItemEight.text = item.typeEight
//            } else {
//                tvItemEight.hide()
//            }
//
//            if (item.typeNine != "") {
//                tvItemNine.text = item.typeNine
//            } else {
//                tvItemNine.hide()
//            }
//
//            if (item.typeTen != "") {
//                tvItemTen.text = item.typeTen
//            } else {
//                tvItemTen.hide()
//            }
//        }
//
////        private fun bindFriend(item: DataModel.Friend) {
////            //Do your view assignment here from the data model
////            itemView.findViewById<AppCompatTextView>(R.id.tvName)?.text = item.name
////            itemView.findViewById<AppCompatTextView>(R.id.tvGender)?.text = item.gender
////        }
//    }

    private inner class GfgViewOne(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvItemOne: TextView = itemView.findViewById(R.id.tvItemOne)
//        val tvItemTwo: TextView = itemView.findViewById(R.id.tvItemTwo)
//        val tvItemThree: TextView = itemView.findViewById(R.id.tvItemThree)
//        val tvItemFour: TextView = itemView.findViewById(R.id.tvItemFour)
//        val tvItemFive: TextView = itemView.findViewById(R.id.tvItemFive)
//        val tvItemSix: TextView = itemView.findViewById(R.id.tvItemSix)
//        val tvItemSeven: TextView = itemView.findViewById(R.id.tvItemSeven)
//        val tvItemEight: TextView = itemView.findViewById(R.id.tvItemEight)
//        val tvItemNine: TextView = itemView.findViewById(R.id.tvItemNine)
//        val tvItemTen: TextView = itemView.findViewById(R.id.tvItemTen)
//        val vwLine: View = itemView.findViewById(R.id.vwLine)
        fun bind(position: Int) {
            val recyclerViewModel = mList[position]
//            tvItemOne.text = recyclerViewModel.typeOne
        }
    }

    private inner class View2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemOne: TextView = itemView.findViewById(R.id.tvTitleOne)
        fun bind(position: Int) {
            val recyclerViewModel = mList[position]
            tvItemOne.text = recyclerViewModel.typeOne
        }
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun updateList(lstItem: List<ItemTable>) {
        mList.addAll(lstItem)
        notifyDataSetChanged()
    }

    companion object {
        const val TYPE_CHART = 0
        const val TYPE_TABLE = 1
    }
}
