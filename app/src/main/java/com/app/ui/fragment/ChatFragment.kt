package com.app.ui.fragment

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.FragmentChatBinding
import com.app.ui.activity.ChatActivity
import com.app.ui.adapter.ChatRoomAdapter
import com.core.ui.BaseFragment
import com.domain.model.ChatMessageData
import com.domain.model.ChatRoomItemData
import com.presentation.ChatFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {
    private val viewModel : ChatFragmentViewModel by viewModels()
    private val adapter by lazy { ChatRoomAdapter() }

    override fun setUi() {
        binding.viewModel = viewModel
        bindingRecyclerView()
    }

    override fun setUpDate() {
        viewModel.getChatRoomData()
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.chatRoomList.observe(lifecycleOwner, ::updateList)

    }

    private fun bindingRecyclerView() {
        binding.recyclerChatList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@ChatFragment.adapter
        }

        adapter.setOnItemClickListener { item, position ->
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra(getString(R.string.friend_detail_chat_extra_code_key), item.chatCode)
            startActivity(intent)
        }
    }

    private fun updateList(list: List<ChatRoomItemData>){
        adapter.submitList(list)
    }
}