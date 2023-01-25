package com.smartfren.smartops.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smartfren.smartops.dao.SiteInfoDao
import com.smartfren.smartops.dao.SiteVisitDao
import com.smartfren.smartops.entity.SiteInfo
import com.smartfren.smartops.entity.SiteVisit

//Database annotation to specify the entities and set version
@Database(entities = [SiteVisit::class, SiteInfo::class], version = 1, exportSchema = false)
abstract class SiteVisitRoomDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: SiteVisitRoomDatabase? = null

        fun getDatabase(context: Context): SiteVisitRoomDatabase {
            return INSTANCE
                ?: synchronized(this) {
                    // Create database here
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        SiteVisitRoomDatabase::class.java,
                        "sitevisit_db"
                    )
                        .allowMainThreadQueries() //allows Room to executing task in main thread
                        .fallbackToDestructiveMigration() //allows Room to recreate database if no migrations found
                        .build()

                    INSTANCE = instance
                    instance
                }
        }
    }

    abstract fun getSiteVisitDao(): SiteVisitDao
    abstract fun getSiteInfoDao(): SiteInfoDao

}