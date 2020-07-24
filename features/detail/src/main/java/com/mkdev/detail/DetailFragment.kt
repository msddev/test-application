package com.mkdev.detail

import android.os.Bundle
import android.view.View
import com.mkdev.core_framework.base.BaseFragment
import com.mkdev.core_framework.extention.popBackStack
import kotlinx.android.synthetic.main.detail_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : BaseFragment(R.layout.detail_fragment) {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private val detailViewModel: DetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        detailToolbar.setNavigationOnClickListener {
            popBackStack()
        }
    }
}