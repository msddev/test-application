package com.mkdev.home.views

import com.mkdev.core_framework.common.ui.AdapterItemLayout
import com.mkdev.core_framework.common.ui.EasyAdapter
import com.mkdev.home.R
import com.mkdev.model.Photo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_home_photo.view.*

internal class HomeAdapter(onItemClicked: (Photo) -> Unit) : EasyAdapter<Photo>() {
    init {
        addLayout(
            AdapterItemLayout(Photo::class, R.layout.item_home_photo) { item, _ ->

                Picasso.get()
                    .load(item.thumbnailUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(homeItemImage)

                setOnClickListener {
                    onItemClicked(item)
                }
            }
        )
    }
}