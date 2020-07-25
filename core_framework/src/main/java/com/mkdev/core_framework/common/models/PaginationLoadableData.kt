package com.mkdev.core_framework.common.models

sealed class PaginationLoadableData<out T>(open val page: Int, open val limit: Int) {
    abstract val data: T?
}

fun <T> PaginationLoadableData<T>.fold(
    onNotLoading: () -> Unit = {},
    onInitialLoading: () -> Unit = {},
    onInitialLoad: (data: T, hasMorePages: Boolean) -> Unit = { _, _ -> },
    onInitialFailed: (throwable: Throwable, title: String?) -> Unit = { _, _ -> },
    onPageLoading: (data: T) -> Unit = {},
    onPageLoad: (data: T, addedCount: Int, hasMorePages: Boolean) -> Unit = { _, _, _ -> },
    onPageFailed: (data: T, throwable: Throwable, title: String?) -> Unit = { _, _, _ -> }
): PaginationLoadableData<T> {
    when (this) {

        is PageInitialFailed -> {
            onInitialFailed(throwable, title)
        }
        is PageInitialLoading -> {
            onInitialLoading()
        }
        is PageInitialNotLoaded -> {
            onNotLoading()
        }
        is PageInitialLoad -> {
            onInitialLoad(data, hasMorePages)
        }
        is PageLoad -> {
            onPageLoad(data, addedCount, hasMorePages)
        }
        is PageLoading -> {
            onPageLoading(data)
        }
        is PageFailed -> {
            onPageFailed(data, throwable, title)
        }
    }
    return this

}

fun <T> PaginationLoadableData<T>.toLoading(): PaginationLoadableData<T> {
    return when (this) {
        is PageInitialNotLoaded, is PageInitialFailed, is PageInitialLoading -> PageInitialLoading(
            this.page,
            this.limit
        )
        else -> PageLoading(this.page, this.limit, this.data!!)
    }
}

fun <T> PaginationLoadableData<T>.toFailed(
    throwable: Throwable,
    title: String?
): PaginationLoadableData<T> {
    return when (this) {
        is PageInitialNotLoaded, is PageInitialFailed, is PageInitialLoading -> PageInitialFailed(
            this.page,
            this.limit,
            throwable,
            title
        )
        else -> PageFailed(data!!, throwable, title, page, limit)
    }
}

fun <T> PaginationLoadableData<T>.toLoaded(
    data: T,
    page: Int,
    addedCount: Int,
    hasMorePages: Boolean
): PaginationLoadableData<T> {
    return when (this) {
        is PageInitialNotLoaded, is PageInitialFailed, is PageInitialLoading -> PageInitialLoad(
            data,
            hasMorePages,
            page,
            this.limit
        )
        else -> PageLoad(page, limit, data, addedCount, hasMorePages)
    }
}

data class PageInitialFailed(
    override val page: Int,
    override val limit: Int,
    val throwable: Throwable,
    val title: String? = null
) : PaginationLoadableData<Nothing>(page, limit) {
    override val data: Nothing?
        get() = null
}

data class PageInitialLoading(override val page: Int, override val limit: Int) :
    PaginationLoadableData<Nothing>(page, limit) {
    override val data: Nothing?
        get() = null


}

data class PageInitialNotLoaded(override val page: Int, override val limit: Int) :
    PaginationLoadableData<Nothing>(page, limit) {
    override val data: Nothing?
        get() = null

}


data class PageInitialLoad<T>(
    override val data: T,
    val hasMorePages: Boolean,
    override val page: Int,
    override val limit: Int
) :
    PaginationLoadableData<T>(page, limit) {

}

data class PageLoad<T>(
    override val page: Int,
    override val limit: Int,
    override val data: T,
    val addedCount: Int,
    val hasMorePages: Boolean
) :
    PaginationLoadableData<T>(page, limit) {

}


data class PageLoading<T>(override val page: Int, override val limit: Int, override val data: T) :
    PaginationLoadableData<T>(page, limit) {

}

data class PageFailed<T>(
    override val data: T,
    val throwable: Throwable,
    val title: String? = null,
    override val page: Int,
    override val limit: Int
) : PaginationLoadableData<T>(page, limit) {

}

private fun PaginationLoadableData<*>.nextPageToFetch(): Int {
    return when (this) {
        is PageInitialNotLoaded -> 0
        is PageLoad, is PageInitialLoad -> this.page + 1
        else -> this.page
    }
}

fun PaginationLoadableData<*>.isLoading(): Boolean =
    this is PageInitialLoading || this is PageLoading

fun PaginationLoadableData<*>.isError(): Boolean = this is PageInitialFailed || this is PageFailed
fun PaginationLoadableData<*>.isCompleted(): Boolean {
    return (this is PageLoad && !this.hasMorePages) || (this is PageInitialLoad && !this.hasMorePages)
}