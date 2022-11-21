package com.bikash.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bikash.trelloclone.R
import com.bikash.trelloclone.databinding.ActivityCardDetailsBinding
import com.bikash.trelloclone.databinding.ActivityMembersBinding
import com.bikash.trelloclone.models.Board
import com.bikash.trelloclone.utils.Constants

class CardDetailsActivity : AppCompatActivity() {

    private  lateinit var mBoardDetails: Board
    private var mTaskListPosition = -1
    private var mCardPosition = -1

    private var binding: ActivityCardDetailsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        getIntentData()
        setupActionBar()
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarCardDetailsActivity)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        supportActionBar?.title = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.toolbarCardDetailsActivity?.setNavigationOnClickListener{
            onBackPressed()

        }

    }

    private fun getIntentData(){
        if (intent.hasExtra(Constants.BOARD_DETAILS)){
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAILS)!!
        }
        if (intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)){
            mTaskListPosition = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION,-1)
        }
        if (intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)){
            mCardPosition = intent.getIntExtra(Constants.CARD_LIST_ITEM_POSITION,-1)
        }
    }
}

