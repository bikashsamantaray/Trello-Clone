package com.bikash.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bikash.trelloclone.R
import com.bikash.trelloclone.databinding.ActivityCreateBoardBinding
import com.bikash.trelloclone.databinding.ActivityMyProfileBinding

class CreateBoardActivity : AppCompatActivity() {
    private var binding: ActivityCreateBoardBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarCreateBoardActivity)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        supportActionBar?.title = resources.getString(R.string.create_board_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.toolbarCreateBoardActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

    }
}