package com.smartfren.smartops.dao

import androidx.room.*
import com.smartfren.smartops.entity.SiteVisit

@Dao
interface SiteVisitDao {

    @Insert
    fun insert(sitevisit: SiteVisit)

    @Update
    fun update(sitevisit: SiteVisit)

    @Delete
    fun delete(sitevisit: SiteVisit)

    @Query("SELECT * FROM sitevisit ORDER BY id DESC")
    fun getAll(): List<SiteVisit>

    @Query("SELECT * FROM sitevisit WHERE id = :id")
    fun getById(id: Int): List<SiteVisit>
}