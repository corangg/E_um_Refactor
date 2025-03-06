package com.app.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.databinding.FragmentProfileBinding
import com.core.ui.BaseFragment
import com.presentation.ProfileFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileFragmentViewModel by viewModels()
    override fun setUi() {
        binding.viewModel = viewModel
    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {

    }
}