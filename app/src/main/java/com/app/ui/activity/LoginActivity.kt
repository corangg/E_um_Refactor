package com.app.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.databinding.ActivityLogInBinding
import com.core.ui.BaseActivity
import com.presentation.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLogInBinding>(ActivityLogInBinding::inflate) {
    private val viewModel: LoginViewModel by viewModels()
    override fun setUi() {
        binding.viewModel = viewModel
        setBinding()
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }

    private fun setBinding(){
        binding.btnSignUp.setOnClickListener{
            startSignUpActivity()
        }
    }

    private fun startSignUpActivity(){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}