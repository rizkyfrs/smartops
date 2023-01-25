package com.smartfren.smartops.ui.dailyactivity

class DateItem : ListItem() {
    var date: String? = null
    override val type: Int
        get() = TYPE_DATE
}