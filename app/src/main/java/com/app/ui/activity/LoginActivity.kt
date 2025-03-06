package com.app.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.ActivityLogInBinding
import com.app.ui.custom.showCustomToast
import com.core.ui.BaseActivity
import com.presentation.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLogInBinding>(ActivityLogInBinding::inflate) {
    private val viewModel: LoginActivityViewModel by viewModels()
    override fun setUi() {
        binding.viewModel = viewModel
        setBinding()
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.signInResult.observe(lifecycleOwner, ::observeSignInResult)
    }

    private fun setBinding() {
        binding.btnSignUp.setOnClickListener {
            startSignUpActivity()
        }
    }

    private fun startSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun observeSignInResult(case: Int) {
        when (case) {
            1 -> startMainActivity()
            2 -> this.showCustomToast(getString(R.string.sign_in_invalid_email))
            3 -> this.showCustomToast(getString(R.string.sign_in_not_found_email))
            4 -> this.showCustomToast(getString(R.string.sign_in_error_password))
            5 -> this.showCustomToast(getString(R.string.sign_in_error))
            else -> {}
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}