package com.mkdev.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.mkdev.core_framework.base.BaseViewModel
import com.mkdev.home.domain.GetPhotosUseCase
import com.mkdev.model.Photo
import com.mkdev.repository.AppDispatchers
import com.mkdev.repository.utils.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val dispatchers: AppDispatchers
) : BaseViewModel() {

    private val _photos = MediatorLiveData<Resource<List<Photo>>>()
    val photos: LiveData<Resource<List<Photo>>> get() = _photos
    private var photosSource: LiveData<Resource<List<Photo>>> = MutableLiveData()

    init {
        getPhotos()
    }

    private fun getPhotos() {
        launch(dispatchers.main) {
            _photos.removeSource(photosSource)
            withContext(dispatchers.io) {
                photosSource = getPhotosUseCase()
            }
            _photos.addSource(photosSource) {
                _photos.value = it
            }
        }
    }
}