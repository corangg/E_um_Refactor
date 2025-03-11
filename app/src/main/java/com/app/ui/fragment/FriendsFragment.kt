package com.app.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.databinding.FragmentFriendsBinding
import com.app.ui.adapter.FriendListAdapter
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
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.friendListData.observe(lifecycleOwner, ::updateFriendList)
    }

    private fun bindingRecyclerView() {
        binding.recyclerFriendsList.apply { layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@FriendsFragment.adapter
        }

        adapter.setOnItemClickListener { item, position ->
        }
    }

    private fun updateFriendList(list: List<FriendItemData>) {
        adapter.submitList(list)
    }
}