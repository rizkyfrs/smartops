package com.smartfren.smartops.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//Entity annotation to specify the table's name
@Entity(tableName = "sitevisit")
//Parcelable annotation to make parcelable object
@Parcelize
data class SiteVisit(
    //PrimaryKey annotation to declare primary key with auto increment value
    //ColumnInfo annotation to specify the column's name
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "sv_site") var sv_site: String? = "",
    @ColumnInfo(name = "sv_num") var sv_num: String? = "",
    @ColumnInfo(name = "sv_date") var sv_date: String? = "",
    @ColumnInfo(name = "sv_reg") var sv_reg: String? = "",
    @ColumnInfo(name = "sv_device_imei") var sv_device_imei: String? = "",
    @ColumnInfo(name = "sv_device_custodian") var sv_device_custodian: String? = "",
    @ColumnInfo(name = "sv_long") var sv_long: String? = "",
    @ColumnInfo(name = "sv_lat") var sv_lat: String? = ""
) : Parcelable
