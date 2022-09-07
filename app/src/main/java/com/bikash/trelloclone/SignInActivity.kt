package com.bikash.trelloclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.bikash.trelloclone.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private var binding: ActivitySignInBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarSignInActivity)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarSignInActivity?.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}