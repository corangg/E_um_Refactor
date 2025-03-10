package com.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.databinding.ItemAddressListBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.domain.model.AddressItemData

class AddressListAdapter : BaseRecyclerView<AddressItemData, AddressListAdapter.AddressListViewHolder>(object :
        DiffUtil.ItemCallback<AddressItemData>() {
        override fun areItemsTheSame(
            oldItem: AddressItemData,
            newItem: AddressItemData
        ) = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: AddressItemData,
            newItem: AddressItemData
        ) = oldItem == newItem
    }) {
    private lateinit var mContext: Context
    private var selectedPosition = 0

    private var onItemDeleteListener: ((Int) -> Unit)? = null
    private var onItemEditListener: ((Int) -> Unit)? = null
    private var onEditModeFlag = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressListViewHolder {
        mContext = parent.context
        return AddressListViewHolder(
            ItemAddressListBinding.inflate(
                LayoutInflater.from(mContext), parent, false
            )
        )
    }

    fun onEditMode() {
        onEditModeFlag = !onEditModeFlag
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AddressListViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.addBind(position, onItemEditListener, onItemDeleteListener)
    }

    fun setOnItemDeleteListener(listener: (Int) -> Unit) {
        onItemDeleteListener = listener
    }

    fun setOnItemEditListener(listener: (Int) -> Unit) {
        onItemEditListener = listener
    }

    inner class AddressListViewHolder(
        private val binding: ItemAddressListBinding
    ) : BaseViewHolder<AddressItemData>(binding) {

        override fun bind(
            item: AddressItemData,
            position: Int,
            clickListener: ((AddressItemData, Int) -> Unit)?
        ) {
            binding.textAddressTitle.text = item.title
            binding.textAddress.text = item.address
            if (item.mainValue) {
                binding.textMain.visibility = View.VISIBLE
            }

            if (!onEditModeFlag) {
                binding.btnAddressEdit.visibility = View.GONE
                binding.btnDeleteAddress.visibility = View.GONE

            } else {
                binding.btnAddressEdit.visibility = View.VISIBLE
                binding.btnDeleteAddress.visibility = View.VISIBLE
            }

            binding.layoutItem.setOnClickListener {
                clickListener?.invoke(item, position)
            }
        }

        fun addBind(
            position: Int,
            editListener: ((Int) -> Unit)?,
            deleteListener: ((Int) -> Unit)?
        ) {
            binding.btnAddressEdit.setOnClickListener {
                editListener?.invoke(position)
            }

            binding.btnDeleteAddress.setOnClickListener {
                deleteListener?.invoke(position)
            }
        }
    }
}