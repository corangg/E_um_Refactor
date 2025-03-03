package com.app.ui.activity

import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import com.app.databinding.ActivityMainBinding
import com.core.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun setUi() {
    }

    override fun setUpDate() {
        startLoginActivity()
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }

    private fun startLoginActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}