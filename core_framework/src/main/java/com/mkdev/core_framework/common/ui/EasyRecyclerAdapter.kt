package com.mkdev.core_framework.common.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.mkdev.core_framework.common.ui.PaginatingRecyclerAdapter.Companion.loadingType
import com.mkdev.core_framework.common.ui.PaginatingRecyclerAdapter.Companion.retryType

/**
 * This class represents what 'ViewHolder' for a recycler view item should do.
 * @param layoutId: layoutID of this view binder
 * @param itemType: layout type of this item
 * @param viewHolderAction: the callback that gets called for each item. in it's lambda, `this` will be the view
 * (the inflated layout) and `it` will be the item.ππ
 */
data class RecyclerViewBinder<in ITEM>(
    @LayoutRes val layoutId: Int,
    val itemType: Int,
    val viewHolderAction: View.(ITEM) -> Unit
) {
    companion object {
        fun forLoading(
            @LayoutRes layoutId: Int,
            viewHolderAction: View.(Any) -> Unit
        ): RecyclerViewBinder<Any> {
            return RecyclerViewBinder(
                layoutId,
                loadingType,
                viewHolderAction
            )

        }

        fun forRetry(
            @LayoutRes layoutId: Int,
            viewHolderAction: View.(Any) -> Unit
        ): RecyclerViewBinder<Any> {
            return RecyclerViewBinder(
                layoutId,
                retryType,
                viewHolderAction
            )

        }

    }
}


/**
 * Item holder for easy recycler view adapters
 * @param item: the item that should be used
 * @param itemType: the layout type for this item, should be the same as the desired one that was given to the
 * easy recycler view using RecyclerViewBinder.
 */
data class RecyclerItem<out ITEM>(val item: ITEM, val itemType: Int)

/**
 * Can be used to create adapters for RecyclerViews.
 * If you want an adapter that can handle loadings/retries, you can use {@link PaginatingRecyclerAdapter}.
 **/
open class EasyRecyclerAdapter<ITEM>(open val binderTypes: List<RecyclerViewBinder<ITEM>>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<EasyRecyclerAdapter.EasyHolder<ITEM>>() {
    constructor(vararg binder: RecyclerViewBinder<ITEM>) : this(binder.toList())

    protected val items = mutableListOf<RecyclerItem<ITEM>>()

    override fun getItemViewType(position: Int): Int {
        return items[position].itemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EasyHolder<ITEM> {
        val binder = binderTypes.first { it.itemType == viewType }
        return EasyHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(binder.layoutId, parent, false), binder
        )
    }

    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: EasyHolder<ITEM>, position: Int) {
        holder.bind(items[position])
    }

    /**
     * Updates the items.
     * @param newList: the list to be replaced with the current list
     * @param toggleLoading: if given, it will change the loading status of the recycler view, otherwise it will stay unchanged
     * @param toggleRetry: if given, it will change the retry status of the recycler view, otherwise it will stay unchanged
     */
    open fun update(newList: List<RecyclerItem<ITEM>>) {
        this.items.clear()
        this.items.addAll(newList)
        notifyDataSetChanged()
    }


    class EasyHolder<ITEM>(itemView: View, val binder: RecyclerViewBinder<ITEM>) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(item: RecyclerItem<ITEM>) {
            binder.viewHolderAction(itemView, item.item)
        }
    }
}

/**
 * Can be used to create adapters for RecyclerViews that need pagination with loading/retries.
 * Example use case:
 *
 * To create an adapter:
 *
 * val adapter = PaginatingRecyclerAdapter(listOf(
 * RecyclerViewBinder(R.layout.item_type_first, 1) {
 * title.text = it.title
 * },
 * RecyclerViewBinder(R.layout.item_type_second, 2) {
 * title.text = it.title
 * }
 * ),
 * RecyclerViewBinder(R.layout.loading, 2) {}, //this is the layoutHandler
 * RecyclerViewBinder(R.layout.retry, 2) {}
 * )

 * adapter.update(
 * listOf(
 * RecyclerItem(TestItem("salam"), 1),
 * RecyclerItem(TestItem("test 2"), 1),
 * RecyclerItem(TestItem("سلام به تو زیبا"), 2),
 * RecyclerItem(TestItem("salam"), 2),
 * RecyclerItem(TestItem("wow"), 1),
 * RecyclerItem(TestItem("ooooowi"), 2)
 * ),
 * toggleLoading = true, toggleRetry = true
 * )
 */
open class PaginatingRecyclerAdapter<ITEM : Any>(
    open val itemBinder: List<RecyclerViewBinder<ITEM>>,
    open val loadingBinder: RecyclerViewBinder<Any>,
    open val retryBinder: RecyclerViewBinder<Any>
) : androidx.recyclerview.widget.RecyclerView.Adapter<PaginatingRecyclerAdapter.PaginatingHolder>() {

    open var isShowingLoading = false
        protected set

    open var isShowingRetry = false
        protected set


    protected val items = mutableListOf<RecyclerItem<ITEM>>()

    constructor(
        vararg binder: RecyclerViewBinder<ITEM>,
        loadingBinder: RecyclerViewBinder<Any>,
        retryBinder: RecyclerViewBinder<Any>
    ) : this(binder.toList(), loadingBinder, retryBinder)

    override fun getItemCount(): Int {
        return items.count() + (if (isShowingRetry) 1 else 0) + (if (isShowingLoading) 1 else 0)
    }

    override fun onBindViewHolder(holder: PaginatingHolder, position: Int) {
        when (holder) {
            is PaginatingHolder.Item<*> -> {
                val item = items[position]
                (holder as PaginatingHolder.Item<ITEM>).bind(item.item)
            }
            is PaginatingHolder.Loading -> {
                holder.bind()
            }
            is PaginatingHolder.Retry -> {
                holder.bind()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaginatingHolder {
        when (viewType) {
            loadingType -> {
                return PaginatingHolder.Loading(
                    LayoutInflater.from(parent.context).inflate(
                        loadingBinder.layoutId,
                        parent,
                        false
                    ), loadingBinder
                )
            }
            retryType -> {
                return PaginatingHolder.Retry(
                    LayoutInflater.from(parent.context).inflate(
                        retryBinder.layoutId,
                        parent,
                        false
                    ), retryBinder
                )
            }
            else -> {
                val binder = itemBinder.first { it.itemType == viewType }
                return PaginatingHolder.Item(
                    LayoutInflater.from(parent.context).inflate(
                        binder.layoutId,
                        parent,
                        false
                    ), binder
                )
            }
        }
    }

    /**
     * Toggles the loading state to 'show'.
     * Note: Retry will be false.
     */
    fun showLoading() {
        if (isShowingLoading)
            return
        isShowingRetry = false
        isShowingLoading = true
        notifyDataSetChanged()
    }

    /**
     * Toggles the retry state to 'show'.
     * Note: Loading will be false.
     */
    fun showRetry() {
        if (isShowingRetry)
            return
        isShowingLoading = false
        isShowingRetry = true
        notifyDataSetChanged()
    }

    /**
     * Removes loading and retry states.
     */
    fun hideAll() {
        isShowingRetry = false
        isShowingLoading = false
        notifyDataSetChanged()
    }


    /**
     * Updates the items.
     * @param newList: the list to be replaced with the current list
     * @param toggleLoading: if given, it will change the loading status of the recycler view, otherwise it will stay unchanged
     * @param toggleRetry: if given, it will change the retry status of the recycler view, otherwise it will stay unchanged
     */
    open fun update(
        newList: List<RecyclerItem<ITEM>>,
        toggleLoading: Boolean? = null,
        toggleRetry: Boolean? = null
    ) {
        this.items.clear()
        this.items.addAll(newList)
        toggleLoading?.takeIf { it != isShowingLoading }?.let {
            isShowingLoading = it
        }
        toggleRetry?.takeIf { it != isShowingRetry }?.let {
            isShowingRetry = it
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val itemsCount = items.count()
        return if (position < itemsCount) items[position].itemType else {
            when (val offset = position - itemsCount) {
                0 -> if (isShowingLoading) loadingType else retryType
                else -> retryType
            }

        }
    }

    /**
     * If you want to use getItemId, this function can be helpful to understand if a position, if of loading type or
     * retry type
     * @param position: the position of the item in the adapter
     */
    protected fun isItemLoading(position: Int): Boolean {
        return getItemViewType(position) == loadingType
    }

    /**
     * If you want to use getItemId, this function can be helpful to understand if a position, if of loading type or
     * retry type
     * @param position: the position of the item in the adapter
     */
    protected fun isItemRetry(position: Int): Boolean {
        return getItemViewType(position) == retryType
    }

    sealed class PaginatingHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        data class Item<in ITEM>(val view: View, val binder: RecyclerViewBinder<ITEM>) :
            PaginatingHolder(view) {
            fun bind(item: ITEM) {
                binder.viewHolderAction(itemView, item)
            }
        }

        data class Loading(val view: View, val loadingBinder: RecyclerViewBinder<Any>) :
            PaginatingHolder(view) {

            fun bind() {
                loadingBinder.viewHolderAction(itemView, Unit)
            }
        }

        data class Retry(val view: View, val retryBinder: RecyclerViewBinder<Any>) :
            PaginatingHolder(view) {

            fun bind() {
                retryBinder.viewHolderAction(itemView, Unit)
            }
        }
    }

    companion object {
        const val loadingType = -1
        const val retryType = -2
    }
}


/**
 * Callback listener for reaching the end of a recyclerview
 */
typealias OnLoadMoreListener = (LoadMoreScrollListener) -> Unit

/**
 * Adds pagination-like load-more capabilities to a RecyclerView.
 * @param loadMoreFunction: the function that is called when recyclerView's bottom is reached.
 * @param visibleItemsThreshold: number of items that will trigger the loadMoreFunction before reaching the bottom
 *
 * Note: You need to set canLoadMore to false when you don't want callbacks, for example when you are loading the
 * 'more' data, you need to set it to false, and after you got the data (or retrieved an error), you need to set it
 * to true.
 */
class LoadMoreScrollListener(
    var visibleItemsThreshold: Int = 4,
    private val loadMoreFunction: OnLoadMoreListener
) :
    androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
    var canLoadMore: Boolean = true
    override fun onScrolled(
        recyclerView: androidx.recyclerview.widget.RecyclerView,
        dx: Int,
        dy: Int
    ) {
        super.onScrolled(recyclerView, dx, dy)
        if (!canLoadMore)
            return
        val shouldCallLoadMore: Boolean = when (val layoutManager = recyclerView.layoutManager) {
            is androidx.recyclerview.widget.LinearLayoutManager -> {
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val totalItems = layoutManager.itemCount
                totalItems <= lastVisibleItem + visibleItemsThreshold
            }
            is androidx.recyclerview.widget.GridLayoutManager -> {
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val totalItems = layoutManager.itemCount
                totalItems <= lastVisibleItem + visibleItemsThreshold
            }
            else -> false

        }
        if (shouldCallLoadMore)
            loadMoreFunction(this)
    }
}

/**
 * Adds pagination-like load-more capabilities to a RecyclerView.
 * @param loadMoreFunction: the function that is called when recyclerView's bottom is reached.
 * @param visibleItemsThreshold: number of items that will trigger the loadMoreFunction before reaching the bottom
 *
 * Note: You need to set canLoadMore to false when you don't want callbacks, for example when you are loading the
 * 'more' data, you need to set it to false, and after you got the data (or retrieved an error), you need to set it
 * to true.
 */
fun androidx.recyclerview.widget.RecyclerView.addLoadMoreListener(
    visibleItemsThreshold: Int = 4,
    loadMoreFunction: OnLoadMoreListener
): LoadMoreScrollListener {
    val loadMoreScrollListener = LoadMoreScrollListener(
        visibleItemsThreshold,
        loadMoreFunction
    )
    addOnScrollListener(loadMoreScrollListener)
//    loadMoreScrollListener.onScrolled(this, 0,0)
    return loadMoreScrollListener
}