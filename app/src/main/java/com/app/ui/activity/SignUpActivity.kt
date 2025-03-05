package com.app.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.databinding.ActivitySignUpBinding
import com.core.ui.BaseActivity
import com.presentation.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {
    private val viewModel: SignUpViewModel by viewModels()
    private val addressResultLauncher = registerForActivityResultHandler { result ->
        if (result.resultCode == RESULT_OK) {
            val zoneCode = result.data?.getStringExtra("zoneCode") ?: ""
            val address = result.data?.getStringExtra("address") ?: ""
            viewModel.setAddress(zoneCode, address)
        }
    }

    override fun setUi() {
        binding.viewModel = viewModel
        setBinding()
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }

    private fun setBinding() {
        binding.btnSearch.setOnClickListener {
            startAddressSearchActivity()
        }
    }

    private fun startAddressSearchActivity() {
        val intent = Intent(this, MapActivity::class.java)
        addressResultLauncher.launch(intent)
    }
}