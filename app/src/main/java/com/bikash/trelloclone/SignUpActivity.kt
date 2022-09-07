package com.bikash.trelloclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bikash.trelloclone.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private var binding: ActivitySignUpBinding? = null
    private var toolbarSignUpActivity: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarSignUpActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarSignUpActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

    }
}