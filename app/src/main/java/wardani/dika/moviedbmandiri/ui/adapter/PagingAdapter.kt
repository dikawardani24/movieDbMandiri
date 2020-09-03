package wardani.dika.moviedbmandiri.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import wardani.dika.moviedbmandiri.R
import wardani.dika.moviedbmandiri.databinding.ItemLoadMoreBinding
import wardani.dika.moviedbmandiri.databinding.ItemLoadMoreErrorBinding
import wardani.dika.moviedbmandiri.databinding.ItemNoMoreDataBinding
import wardani.dika.moviedbmandiri.ui.adapter.viewHolder.LoadMoreErrorViewHolder
import wardani.dika.moviedbmandiri.ui.adapter.viewHolder.LoadingViewHolder
import wardani.dika.moviedbmandiri.ui.adapter.viewHolder.NoMoreDataViewHolder
import wardani.dika.moviedbmandiri.util.MapperList
import wardani.dika.moviedbmandiri.util.PageViewType
import wardani.dika.moviedbmandiri.util.TypeWrapper

abstract class PagingAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var typeWrappers: ArrayList<TypeWrapper<T>> = arrayListOf()
    var onRetryLoadMoreListener: OnRetryLoadMoreListener? = null

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when(PageViewType.toPageViewType(viewType)) {
            PageViewType.DATA -> {
                onCreateViewHolderData(inflater, parent)
            }
            PageViewType.LOADING -> {
                val binding: ItemLoadMoreBinding = DataBindingUtil.inflate(inflater, R.layout.item_load_more, parent, false)
                LoadingViewHolder(binding)
            }
            PageViewType.NO_MORE_DATA -> {
                val binding: ItemNoMoreDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_no_more_data, parent, false)
                NoMoreDataViewHolder(binding)
            }
            PageViewType.ERROR_OCCURED -> {
                val binding: ItemLoadMoreErrorBinding = DataBindingUtil.inflate(inflater, R.layout.item_load_more_error, parent, false)
                LoadMoreErrorViewHolder(binding)
            }
        }
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val typeWrapper = typeWrappers[position]
        if (typeWrapper is TypeWrapper.DataWrapper<T>) {
            onBindViewHolder(holder, typeWrapper.value)
        } else if (typeWrapper is TypeWrapper.ErrorWrapper<T> && holder is LoadMoreErrorViewHolder) {
            val onRetryLoadMoreListener= onRetryLoadMoreListener
            if (onRetryLoadMoreListener != null) {
                holder.bind(onRetryLoadMoreListener)
            }
        }
    }

    final override fun getItemViewType(position: Int): Int {
        val pageViewType = when (typeWrappers[position]) {
            is TypeWrapper.DataWrapper -> PageViewType.DATA
            is TypeWrapper.LoadingWrapper -> PageViewType.LOADING
            is TypeWrapper.NoMoreDataWrapper -> PageViewType.NO_MORE_DATA
            is TypeWrapper.ErrorWrapper -> PageViewType.ERROR_OCCURED
        }

        return pageViewType.value
    }

    final override fun getItemCount(): Int {
        return typeWrappers.size
    }

    fun showLoading() {
        typeWrappers.add(TypeWrapper.LoadingWrapper())
        notifyItemInserted(typeWrappers.size -1)
    }

    fun closeLoading() {
        var positionToRemove: Int = -1

        for(i in 0 until typeWrappers.size) {
            if (typeWrappers[i] is TypeWrapper.LoadingWrapper) {
                positionToRemove = i
                break
            }
        }

        typeWrappers.remove(typeWrappers[positionToRemove])
        notifyItemRemoved(positionToRemove)
    }

    fun showNoMoreData() {
        typeWrappers.add(TypeWrapper.NoMoreDataWrapper())
        notifyItemInserted(typeWrappers.size -1)
    }

    fun closeNoMoreData() {
        val lastPosition = typeWrappers.size -1
        val typeWrapper = typeWrappers[lastPosition]

        if (typeWrapper is TypeWrapper.NoMoreDataWrapper) {
            typeWrappers.remove(typeWrapper)
            notifyItemRemoved(typeWrappers.size)
        }
    }

    fun showError() {
        typeWrappers.add(TypeWrapper.ErrorWrapper())
        notifyItemInserted(typeWrappers.size -1)
    }

    fun closeError() {
        var positionToRemove: Int = -1

        for(i in 0 until typeWrappers.size) {
            if (typeWrappers[i] is TypeWrapper.ErrorWrapper) {
                positionToRemove = i
                break
            }
        }

        typeWrappers.remove(typeWrappers[positionToRemove])
        notifyItemRemoved(positionToRemove)
    }

    fun clear() {
        typeWrappers.clear()
        notifyDataSetChanged()
    }

    fun changeData(newListData: List<T>) {
        val x = MapperList.map(newListData) {
            TypeWrapper.DataWrapper(it)
        }

        typeWrappers = arrayListOf<TypeWrapper<T>>().apply {
            addAll(x)
        }
    }

    interface OnRetryLoadMoreListener {
        fun onRetryLoadMore()
    }

    protected abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, value: T)

    protected abstract fun onCreateViewHolderData(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder
}