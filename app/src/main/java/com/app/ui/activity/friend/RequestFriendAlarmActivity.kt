package com.app.ui.activity.friend

import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.ActivityRequestFriendAlarmBinding
import com.app.ui.adapter.RequestFriendAlarmAdapter
import com.app.ui.custom.showCustomToast
import com.core.ui.BaseActivity
import com.presentation.RequestFriendAlarmViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestFriendAlarmActivity :
    BaseActivity<ActivityRequestFriendAlarmBinding>(ActivityRequestFriendAlarmBinding::inflate) {
    private val viewModel: RequestFriendAlarmViewModel by viewModels()
    private val adapter by lazy { RequestFriendAlarmAdapter() }

    override fun setUi() {
        bindingOnClick()
        bindingRecyclerView()
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.friendRequestAlarm.observe(lifecycleOwner, ::setRecyclerViewList)
        viewModel.onResponseResult.observe(lifecycleOwner, ::showResultMessage)
    }

    private fun bindingOnClick() {
        binding.btnBackActivity.setOnClickListener { finish() }
    }

    private fun bindingRecyclerView() {
        binding.recyclerFriendRequestAlarmList.apply {
            layoutManager = LinearLayoutManager(this@RequestFriendAlarmActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@RequestFriendAlarmActivity.adapter
        }

        adapter.setOnItemResponseListener { position, value ->
            viewModel.responseFriendRequest(position, value)
        }
    }

    private fun setRecyclerViewList(list: List<String>) {
        adapter.submitList(list)
    }

    private fun showResultMessage(result: Boolean){
        if(result) showCustomToast(getString(R.string.add_friend_request_alarm_toast))
    }
}