package com.smartfren.smartops.ui.dailyactivity

class GeneralItem(name: String) : ListItem() {
    var pojoOfJsonArray: PojoOfJsonArray? = null
    override val type: Int
        get() = TYPE_GENERAL
}