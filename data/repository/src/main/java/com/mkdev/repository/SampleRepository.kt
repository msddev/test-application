package com.mkdev.repository

import androidx.lifecycle.LiveData
import com.mkdev.local.dao.SampleDao
import com.mkdev.model.Photo
import com.mkdev.remote.SampleDataSource
import com.mkdev.repository.utils.NetworkBoundResource
import com.mkdev.repository.utils.Resource
import kotlinx.coroutines.Deferred

interface SampleRepository {
    suspend fun getPhotosWithCache(forceRefresh: Boolean = false): LiveData<Resource<List<Photo>>>
}

class SampleRepositoryImpl(
    private val dataSource: SampleDataSource,
    private val dao: SampleDao
) : SampleRepository {

    /**
     * Suspended function that will get a list of [Photo]
     * whether in cache (SQLite) or via network (API).
     * [NetworkBoundResource] is responsible to handle this behavior.
     */
    override suspend fun getPhotosWithCache(forceRefresh: Boolean): LiveData<Resource<List<Photo>>> {
        return object : NetworkBoundResource<List<Photo>, List<Photo>>() {
            override fun processResponse(response: List<Photo>): List<Photo> = response

            override suspend fun saveCallResults(items: List<Photo>) = dao.save(items)

            override fun shouldFetch(data: List<Photo>?): Boolean =
                data == null || data.isEmpty() || forceRefresh

            override suspend fun loadFromDb(): List<Photo> = dao.getPhotos()

            override fun createCallAsync(): Deferred<List<Photo>> = dataSource.fetchPhotosAsync()

        }.build().asLiveData()
    }
}