package com.smartfren.smartops.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//Entity annotation to specify the table's name
@Entity(tableName = "siteinfo")
//Parcelable annotation to make parcelable object
@Parcelize
data class SiteInfo(
    //PrimaryKey annotation to declare primary key with auto increment value
    //ColumnInfo annotation to specify the column's name
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "site_info") var site_info: String? = "",
) : Parcelable
