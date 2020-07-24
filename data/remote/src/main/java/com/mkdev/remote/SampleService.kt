package com.mkdev.remote

import com.mkdev.model.Photo
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface SampleService {

    @GET("photos")
    fun fetchPhotosAsync(): Deferred<List<Photo>>
}