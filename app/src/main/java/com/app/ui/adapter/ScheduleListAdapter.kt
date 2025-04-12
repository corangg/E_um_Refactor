package com.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.R
import com.app.databinding.ItemScheduleListBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.domain.model.ScheduleData

class ScheduleListAdapter :
    BaseRecyclerView<ScheduleData, ScheduleListAdapter.ScheduleListViewHolder>(object :
        DiffUtil.ItemCallback<ScheduleData>() {
        override fun areItemsTheSame(
            oldItem: ScheduleData,
            newItem: ScheduleData
        ) = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: ScheduleData,
            newItem: ScheduleData
        ) = oldItem == newItem
    }) {
    private lateinit var mContext: Context
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListViewHolder {
        mContext = parent.context
        return ScheduleListViewHolder(
            ItemScheduleListBinding.inflate(
                LayoutInflater.from(mContext), parent, false
            )
        )
    }

    inner class ScheduleListViewHolder(
        private val binding: ItemScheduleListBinding
    ) : BaseViewHolder<ScheduleData>(binding) {

        override fun bind(
            item: ScheduleData,
            position: Int,
            clickListener: ((ScheduleData, Int) -> Unit)?
        ) {
            val defaultMessage = mContext.getString(R.string.schedule_item_nickname)
            val message = "${item.nickname} $defaultMessage"
            val date = item.time.substring(0,8)
            val time = item.time.substring(8,12)

            binding.textItemNickname.text = message
            binding.textDate.text = insertString(date,"-", listOf(4,6))
            binding.textTime.text = insertString(time,":", listOf(2))
            binding.textItemAddress.text = item.scheduleAddress

            if(setAmPm(time)) {
                binding.textAmPm.text = mContext.getString(R.string.schedule_item_am)
            }else{
                binding.textAmPm.text = mContext.getString(R.string.schedule_item_pm)
            }

            binding.itemFriend.setOnClickListener {
                clickListener?.invoke(item, position)
            }
        }

        private fun insertString(original: String, insert: String, positions: List<Int>): String {
            var result = original
            positions.sortedDescending().forEach { pos ->
                if (pos in 0..result.length) {
                    result = result.substring(0, pos) + insert + result.substring(pos)
                }
            }
            return result
        }

        private fun setAmPm(time: String): Boolean {
            return ((time.substring(0, 2).toIntOrNull() ?: 0) <= 12)
        }
    }
}