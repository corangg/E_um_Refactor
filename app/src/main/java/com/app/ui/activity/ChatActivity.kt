package com.app.ui.activity

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.ActivityChatBinding
import com.app.ui.adapter.ChatAdapter
import com.core.ui.BaseActivity
import com.domain.model.ChatMessageData
import com.presentation.ChatActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding>(ActivityChatBinding::inflate) {
    private val viewModel: ChatActivityViewModel by viewModels()
    private val adapter by lazy { ChatAdapter() }

    override fun setUi() {
        bindingRecyclerView()
        binding.viewModel = viewModel
        binding.scrollChat.post {
            binding.scrollChat.fullScroll(View.FOCUS_DOWN)
        }
    }

    override fun setUpDate() {
        setChat()
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.chatMessageList.observe(lifecycleOwner, ::updateChatList)
    }

    private fun bindingRecyclerView() {
        binding.btnBackActivity.setOnClickListener { finish() }
        binding.btnSend.setOnClickListener { sendChat() }
        binding.recyclerAddressList.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@ChatActivity.adapter
        }
    }

    private fun setChat(){
        val chatEmail = intent.getStringExtra(getString(R.string.friend_detail_chat_extra_email_key))?: return
        val chatNickname = intent.getStringExtra(getString(R.string.friend_detail_chat_extra_nickname_key))?: return
        binding.textToolbarTitle.text = chatNickname
        viewModel.getChatData(chatEmail)
        viewModel.test(chatEmail)
    }

    private fun sendChat(){
        val chatEmail = intent.getStringExtra(getString(R.string.friend_detail_chat_extra_email_key))?: return
        binding.editChat.requestFocus()
        binding.scrollChat.fullScroll(View.FOCUS_DOWN)
        viewModel.sendChat(chatEmail)
    }

    private fun updateChatList(list: List<ChatMessageData>) {
        adapter.submitList(list)
    }
}