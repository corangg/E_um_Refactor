package com.core.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseViewHolder<T>(
    binding: ViewDataBinding
) : ViewHolder(binding.root) {
    abstract fun bind(item: T, position: Int, clickListener: ((T, Int) -> Unit)? = null)
}

abstract class BaseRecyclerView<T, VH : BaseViewHolder<T>>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback) {
    private var onItemClickListener: ((T, Int) -> Unit)? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position), position, onItemClickListener)
    }

    fun setOnItemClickListener(listener: (T, Int) -> Unit) {
        onItemClickListener = listener
    }
}