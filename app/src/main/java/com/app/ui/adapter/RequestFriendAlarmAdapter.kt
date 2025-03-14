package com.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.R
import com.app.databinding.ItemRequestFriendAlarmBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder

class RequestFriendAlarmAdapter :
    BaseRecyclerView<String, RequestFriendAlarmAdapter.RequestFriendAlarmViewHolder>(object :
        DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ) = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ) = oldItem == newItem
    }) {
    private lateinit var mContext: Context
    private var selectedPosition = 0

    private var onItemResponseListener: ((Int, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestFriendAlarmViewHolder {
        mContext = parent.context
        return RequestFriendAlarmViewHolder(
            ItemRequestFriendAlarmBinding.inflate(
                LayoutInflater.from(mContext), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RequestFriendAlarmViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.addBind(position, onItemResponseListener)
    }

    fun setOnItemResponseListener(listener: (Int, Boolean) -> Unit) {
        onItemResponseListener = listener
    }

    inner class RequestFriendAlarmViewHolder(
        private val binding: ItemRequestFriendAlarmBinding
    ) : BaseViewHolder<String>(binding) {

        override fun bind(
            item: String,
            position: Int,
            clickListener: ((String, Int) -> Unit)?
        ) {
            val defaultMessage = mContext.getString(R.string.add_friend_request_alarm_item_message)
            val message = "$item $defaultMessage"
            binding.textMessage.text = message
        }

        fun addBind(
            position: Int,
            responseListener: ((Int, Boolean) -> Unit)?
        ) {
            binding.btnOk.setOnClickListener {
                responseListener?.invoke(position, true)
            }

            binding.btnNo.setOnClickListener {
                responseListener?.invoke(position, false)
            }
        }
    }
}