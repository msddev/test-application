package com.mkdev.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mkdev.local.dao.SampleDao
import com.mkdev.model.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class ArchAppDatabase : RoomDatabase() {

    // DAO
    abstract fun photoDao(): SampleDao

    companion object {

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArchAppDatabase::class.java,
                "TestApp.db"
            ).build()
    }
}