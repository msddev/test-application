package com.mkdev.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mkdev.core_framework.base.BaseFragment
import com.mkdev.core_framework.extention.visibleBy
import com.mkdev.home.views.HomeAdapter
import com.mkdev.model.Photo
import com.mkdev.repository.utils.Resource
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(R.layout.home_fragment) {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.photos.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingVisibility(true)
                }
                Resource.Status.SUCCESS -> {
                    loadingVisibility(false)
                    it.data?.let { data -> updateData(data) }
                }
                Resource.Status.ERROR -> {
                    loadingVisibility(false)
                    showError()
                }
            }
        })

        homePhotosList.adapter = HomeAdapter { photo ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToNavGraphDetailFeature(
                    photo.url
                )
            )
        }
    }

    private fun loadingVisibility(isVisible: Boolean) {
        homePhotosList.visibleBy(!isVisible)
        homeLoadingView.visibleBy(isVisible)
    }

    private fun showError() {
        Toast.makeText(requireContext(), R.string.an_error_happened, Toast.LENGTH_LONG).show()
    }

    private fun updateData(it: List<Photo>) {
        (homePhotosList.adapter as HomeAdapter).setItemsAndNotify(it)
    }
}