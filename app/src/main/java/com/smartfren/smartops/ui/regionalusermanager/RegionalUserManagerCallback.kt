package com.smartfren.smartops.ui.regionalusermanager

import androidx.recyclerview.widget.DiffUtil
import com.smartfren.smartops.data.models.RegionalUserManagerListResponse

class RegionalUserManagerCallback(
    private val oldList: List<RegionalUserManagerListResponse.CvRegusermgm>,
    private val newList: List<RegionalUserManagerListResponse.CvRegusermgm>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idUser == newList[newItemPosition].idUser
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].idUser == newList[newItemPosition].idUser -> true
            oldList[oldItemPosition].uName == newList[newItemPosition].uName -> true
            else -> false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}