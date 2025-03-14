package com.app.ui.fragment

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.databinding.FragmentFriendsBinding
import com.app.ui.activity.friend.AddFriendActivity
import com.app.ui.activity.friend.RequestFriendAlarmActivity
import com.app.ui.adapter.FriendListAdapter
import com.bumptech.glide.Glide
import com.core.ui.BaseFragment
import com.domain.model.FriendItemData
import com.presentation.FriendsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFragment : BaseFragment<FragmentFriendsBinding>(FragmentFriendsBinding::inflate) {
    val viewModel: FriendsFragmentViewModel by viewModels()
    private val adapter by lazy { FriendListAdapter() }

    override fun setUi() {
        binding.viewModel = viewModel
        bindingRecyclerView()
        bindingOnClick()
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.friendListData.observe(lifecycleOwner, ::updateFriendList)
        viewModel.userProfileUrlLiveData.observe(lifecycleOwner, ::setProfileImage)
        viewModel.totalAlarmRequests.observe(lifecycleOwner,::setFriendRequestAlarm)
    }

    private fun bindingRecyclerView() {
        binding.recyclerFriendsList.apply { layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@FriendsFragment.adapter
        }

        adapter.setOnItemClickListener { item, position ->
        }
    }

    private fun bindingOnClick() {
        binding.btnAddFriend.setOnClickListener {
            val intent = Intent(requireContext(), AddFriendActivity::class.java)
            startActivity(intent)
        }
        binding.btnAlarmFriend.setOnClickListener {
            val intent = Intent(requireContext(), RequestFriendAlarmActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateFriendList(list: List<FriendItemData>) {
        adapter.submitList(list)
    }

    private fun setFriendRequestAlarm(alarmSize: Int) {
        binding.viewFriendRequestAlarm.visibility = if (alarmSize > 0) View.VISIBLE else View.GONE
    }


    private fun setProfileImage(uri: String) {
        if (uri.isEmpty()) return
        Glide.with(binding.root).load(uri).into(binding.imgProfile)
    }
}