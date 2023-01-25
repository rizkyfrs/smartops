package com.smartfren.smartops.data.models

interface Item {
    fun getItemType(): Int
}

const val PARENT = 0
const val CHILD = 1

data class Parent(val id: Int, val title: String, val image: Int) : Item {
    val childItems = ArrayList<Child>()
    var isExpanded = false
    var selectedChild: Child? = null

    override fun getItemType() = PARENT
}

data class Child(
    val parent: Parent,
    val id: Int,
    val title: String,
    var image: Int) : Item {

    override fun getItemType() = CHILD
}