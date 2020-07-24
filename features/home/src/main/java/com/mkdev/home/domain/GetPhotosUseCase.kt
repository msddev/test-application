package com.mkdev.home.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mkdev.model.Photo
import com.mkdev.repository.SampleRepository
import com.mkdev.repository.utils.Resource

/**
 * Use case that gets a [Resource][List][Photo] from [SampleRepository]
 * and makes some specific logic actions on it.
 *
 * In this Use Case, I'm just doing nothing... ¯\_(ツ)_/¯
 */
class GetPhotosUseCase(private val repository: SampleRepository) {

    suspend operator fun invoke(forceRefresh: Boolean = false): LiveData<Resource<List<Photo>>> {
        return Transformations.map(repository.getPhotosWithCache(forceRefresh)) {
            it // Place here your specific logic actions
        }
    }
}