package com.app.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.databinding.FragmentChatBinding
import com.core.ui.BaseFragment
import com.domain.model.ChatMessageData
import com.domain.model.ChatRoomItemData
import com.presentation.ChatFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {
    val viewModel : ChatFragmentViewModel by viewModels()

    override fun setUi() {
        binding.viewModel = viewModel

    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.chatRoomList.observe(lifecycleOwner, ::test)

    }

    private fun test(list: List<ChatRoomItemData>){
        list
        true
    }

}