package com.mkdev.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.mkdev.core_framework.base.BaseFragment
import com.mkdev.core_framework.extention.popBackStack
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(R.layout.home_fragment) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeOpenDetailButton.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToNavGraphDetailFeature(
                    "url"
                )
            )
        }
    }
}