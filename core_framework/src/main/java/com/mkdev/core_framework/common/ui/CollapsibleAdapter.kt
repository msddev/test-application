package com.mkdev.core_framework.common.ui

import androidx.recyclerview.widget.DiffUtil
import kotlin.reflect.KClass

open class CollapsibleAdapter<ITEM : Any>(private val parentType: KClass<*>) : EasyAdapter<ITEM>() {
    private val actualItems = mutableListOf<ITEM>()

    //    private val visibleItems = mutableListOf<ITEM>()
    private val collapsedItems = mutableSetOf<ITEM>()

    fun collapse(item: ITEM) {
        synchronized(items) {
            if (collapsedItems.contains(item)) return
            items.indexOfFirst { it == item }.takeIf { it >= 0 }?.let { currentPosition ->
                val slicedItems = items.toList().slice(currentPosition + 1 until items.size)
                val slicedIndex =
                    slicedItems.indexOfFirst { it::class == parentType }.takeIf { it >= 0 }
                        ?: slicedItems.size
                collapsedItems.add(item)
                val nextParentPosition = currentPosition + 1 + slicedIndex
                val newVisibleItems =
                    items.toList().slice((0..currentPosition)) + items.toList().slice(
                        nextParentPosition until items.size
                    )
                items.clear()
                items.addAll(newVisibleItems)
                notifyItemChanged(currentPosition)
                notifyItemRangeRemoved(
                    currentPosition + 1,
                    nextParentPosition - currentPosition - 1
                )


            }
        }
    }

    fun expand(item: ITEM) {
        synchronized(items) {
            if (!collapsedItems.contains(item)) return

            val positionInCurrentItems = items.indexOfFirst { it == item }
            actualItems.indexOfFirst { it == item }.takeIf { it >= 0 }?.let { currentPosition ->
                val slicedItems =
                    actualItems.toList().slice(currentPosition + 1 until actualItems.size)

                val slicedIndex =
                    slicedItems.indexOfFirst { it::class == parentType }.takeIf { it >= 0 }
                        ?: slicedItems.size
                val nextParentPosition = currentPosition + 1 + slicedIndex
                val newVisibleItems =
                    actualItems.toList().slice(currentPosition + 1 until nextParentPosition)
                val newItems = items.toMutableList()
                newVisibleItems.forEachIndexed { index, t ->
                    newItems.add(positionInCurrentItems + index + 1, t)
                }
                items.clear()
                items.addAll(newItems)
                notifyItemChanged(positionInCurrentItems)
                notifyItemRangeInserted(currentPosition + 1, newVisibleItems.count())


            }
            collapsedItems.remove(item)
        }

    }

    fun toggle(item: ITEM) {
        if (collapsedItems.contains(item)) {
            expand(item)
        } else {
            collapse(item)
        }
    }

    override fun setItems(newList: List<ITEM>, diffResult: DiffUtil.DiffResult) {
        val currentlyRemovedItems = actualItems - items
        actualItems.clear()
        actualItems.addAll(newList)
        items.addAll(actualItems - currentlyRemovedItems)
        super.setItems(newList, diffResult)
    }

    override fun setItemsAndNotify(newList: List<ITEM>) {
        val currentlyRemovedItems = actualItems - items
        actualItems.clear()
        actualItems.addAll(newList)
        items.addAll(actualItems - currentlyRemovedItems)
        super.setItemsAndNotify(newList)

    }

    override fun onViewRecycled(holder: ViewHolder) {
    }

    fun isItemCollapsed(item: ITEM): Boolean = collapsedItems.contains(item)
}