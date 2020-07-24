package com.mkdev.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.mkdev.model.Photo

@Dao
abstract class SampleDao : BaseDao<Photo>() {

    @Query("SELECT * FROM Photo LIMIT 30")
    abstract suspend fun getPhotos(): List<Photo>

    @Query("SELECT * FROM Photo WHERE id = :id LIMIT 1")
    abstract suspend fun getPhoto(id: String): Photo

    suspend fun save(photo: Photo) {
        insert(photo)
    }

    suspend fun save(photos: List<Photo>) {
        insert(photos)
    }
}