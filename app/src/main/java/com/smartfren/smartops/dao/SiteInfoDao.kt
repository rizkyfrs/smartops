package com.smartfren.smartops.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.smartfren.smartops.entity.SiteInfo

@Dao
interface SiteInfoDao {

    @Insert(onConflict = REPLACE)
    fun insert(siteinfo: SiteInfo)

    @Insert
    fun insertAll(siteinfo: List<SiteInfo>)

    @Update
    fun update(siteinfo: SiteInfo)

    @Query("DELETE FROM siteinfo")
    fun delete()

    @Query("SELECT site_info FROM siteinfo")
    fun getAll(): MutableList<String>

    @Query("SELECT * FROM sitevisit WHERE id = :id")
    fun getById(id: Int): List<SiteInfo>
}