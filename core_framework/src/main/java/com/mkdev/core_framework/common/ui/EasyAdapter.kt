package com.mkdev.core_framework.common.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

data class AdapterItemLayout<T : Any>(
    val clazz: KClass<T>,
    val layout: Int,
    val onRecycled: (View.(data: T) -> Unit) = {},
    val handler: View.(data: T, position: Int) -> Unit
)


open class EasyAdapter<T : Any> : RecyclerView.Adapter<EasyAdapter.ViewHolder>(
) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    open val items = mutableListOf<T>()
    protected val itemLayouts = mutableListOf<AdapterItemLayout<T>>()
    protected val itemTypes = mutableMapOf<Int, AdapterItemLayout<T>>()
    private val viewHolderOldPositions = mutableMapOf<ViewHolder, Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = itemTypes[viewType]!!.layout
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    fun <X : T> addLayout(classHandler: AdapterItemLayout<X>) {
        itemLayouts.add(classHandler as AdapterItemLayout<T>)
        itemTypes[classHandler.hashCode()] = classHandler
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolderOldPositions[holder] = position
        itemLayouts.first { it.clazz == items[position]::class }
            .handler(holder.itemView, items[position], holder.adapterPosition)
    }


    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)

        val position = viewHolderOldPositions[holder] ?: return
        itemLayouts.first { it.clazz == items[position]::class }
            .onRecycled(holder.itemView, items[position])

    }

    override fun getItemViewType(position: Int): Int {
        return itemTypes.keys.first { key ->
            key == itemLayouts.first { it.clazz == items[position]::class }.hashCode()
        }
    }

    open fun setItemsAndNotify(newList: List<T>) {
        viewHolderOldPositions.clear()
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    open fun setItems(newList: List<T>, diffResult: DiffUtil.DiffResult) {
        viewHolderOldPositions.clear()
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}