package com.bikash.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bikash.trelloclone.R
import com.bikash.trelloclone.databinding.ActivityMyProfileBinding

class MyProfileActivity : BaseActivity() {
    private var binding: ActivityMyProfileBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarMyProfile)
        binding?.toolbarMyProfile?.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        supportActionBar?.title = resources.getString(R.string.my_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.toolbarMyProfile?.setNavigationOnClickListener{
            onBackPressed()
        }

    }

}