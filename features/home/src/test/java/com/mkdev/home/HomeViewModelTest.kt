package com.mkdev.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.mkdev.home.domain.GetPhotosUseCase
import com.mkdev.model.Photo
import com.mkdev.repository.AppDispatchers
import com.mkdev.repository.utils.Resource
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
class HomeViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var getPhotosUseCase: GetPhotosUseCase
    private lateinit var homeViewModel: HomeViewModel
    private val appDispatchers = AppDispatchers(Dispatchers.Unconfined, Dispatchers.Unconfined)

    private val FAKE_USERS = listOf(
        Photo(
            id = 1,
            albumId = 1,
            url = "https://via.placeholder.com/600/771796",
            thumbnailUrl = "https://via.placeholder.com/150/771796",
            title = "title"
        ),
        Photo(
            id = 2,
            albumId = 1,
            url = "https://via.placeholder.com/600/771796",
            thumbnailUrl = "https://via.placeholder.com/150/771796",
            title = "title"
        ),
        Photo(
            id = 3,
            albumId = 1,
            url = "https://via.placeholder.com/600/771796",
            thumbnailUrl = "https://via.placeholder.com/150/771796",
            title = "title"
        )
    )

    @Before
    fun setUp() {
        getPhotosUseCase = mockk()
    }

    @Test
    fun `Photos requested when ViewModel is created`() {
        val observer = mockk<Observer<Resource<List<Photo>>>>(relaxed = true)
        val result = Resource.success(FAKE_USERS)
        coEvery { getPhotosUseCase(any()) } returns MutableLiveData<Resource<List<Photo>>>().apply {
            value = result
        }

        homeViewModel = HomeViewModel(getPhotosUseCase, appDispatchers)
        homeViewModel.photos.observeForever(observer)

        verify {
            observer.onChanged(result)
        }

        confirmVerified(observer)
    }

    @Test
    fun `Photos requested but failed when ViewModel is created`() {
        val observer = mockk<Observer<Resource<List<Photo>>>>(relaxed = true)
        val result = Resource.error(Exception("fail"), null)
        coEvery { getPhotosUseCase(any()) } returns MutableLiveData<Resource<List<Photo>>>().apply {
            value = result
        }

        homeViewModel = HomeViewModel(getPhotosUseCase, appDispatchers)
        homeViewModel.photos.observeForever(observer)

        verify {
            observer.onChanged(result)
        }

        confirmVerified(observer)
    }
}