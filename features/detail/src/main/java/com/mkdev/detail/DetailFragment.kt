package com.mkdev.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.mkdev.core_framework.base.BaseFragment
import com.mkdev.core_framework.extention.popBackStack
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : BaseFragment(R.layout.detail_fragment) {

    private val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get()
            .load(args.image)
            .placeholder(R.drawable.placeholder)
            .into(detailItemImage)

        detailToolbar.setNavigationOnClickListener {
            popBackStack()
        }
    }
}