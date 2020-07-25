package com.mkdev.core_framework.common.ui.recyclerview.stickyheader

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mkdev.core_framework.common.ui.EasyAdapter
import kotlin.reflect.KClass

open class StickyHeaderRecyclerView<ITEM : Any> constructor(private val stickyType: KClass<*>) :
    EasyAdapter<ITEM>(),
    StickyHeaderHandler {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val stickHeaderDecoration = StickHeaderItemDecoration(this)
        recyclerView.addItemDecoration(stickHeaderDecoration)
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        for (pos in itemPosition downTo 0) {
            if (isHeader(pos)) {
                return pos
            }
        }
        return 0
    }

    override fun getHeaderLayout(headerPosition: Int) =
        itemLayouts.first { it.clazz == items[headerPosition]::class }.layout


    override fun bindHeaderData(header: View, headerPosition: Int) {
        itemLayouts.first { it.clazz == items[headerPosition]::class }
            .handler(header, items[headerPosition], headerPosition)
    }

    override fun isHeader(itemPosition: Int) = items[itemPosition]::class == stickyType


}