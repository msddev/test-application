package com.mkdev.remote

/**
 * Implementation of [SampleService] interface
 */
class SampleDataSource(private val sampleService: SampleService) {

    fun fetchPhotosAsync() = sampleService.fetchPhotosAsync()
}