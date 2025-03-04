package com.app.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.ActivityMainBinding
import com.app.databinding.ActivitySignUpBinding
import com.core.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {
    override fun setUi() {
    }

    override fun setUpDate() {
        startLoginActivity()
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }

    private fun startLoginActivity(){
    }
}