package com.app.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.ActivitySignUpBinding
import com.app.ui.custom.showCustomToast
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
        viewModel.trySignUpResult.observe(lifecycleOwner, ::showSignUpToast)
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

    private fun showSignUpToast(value: Int) {
        when (value) {
            1 -> this.showCustomToast(getString(R.string.signup_toast_1))
            2 -> this.showCustomToast(getString(R.string.signup_toast_2))
            3 -> this.showCustomToast(getString(R.string.signup_toast_3))
            4 -> this.showCustomToast(getString(R.string.signup_toast_4))
            5 -> this.showCustomToast(getString(R.string.signup_toast_5))
            6 -> {
                this.showCustomToast(getString(R.string.signup_toast_6))
                finish()
            }

            7 -> this.showCustomToast(getString(R.string.signup_toast_7))
        }

    }
}