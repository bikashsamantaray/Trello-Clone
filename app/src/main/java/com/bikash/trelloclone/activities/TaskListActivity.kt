package com.bikash.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bikash.trelloclone.R
import com.bikash.trelloclone.databinding.ActivityMyProfileBinding
import com.bikash.trelloclone.databinding.ActivityTaskListBinding
import com.bikash.trelloclone.firebase.FireStoreClass
import com.bikash.trelloclone.models.Board
import com.bikash.trelloclone.utils.Constants

class TaskListActivity : BaseActivity() {

    private var binding: ActivityTaskListBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var boardDocumentId = ""
        if (intent.hasExtra(Constants.DOCUMENT_ID)){
            boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID).toString()

        }
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getBoardSDetails(this@TaskListActivity, boardDocumentId)

    }

    private fun setupActionBar(title: String){
        setSupportActionBar(binding?.toolbarTaskListActivity)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.toolbarTaskListActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

    }

    fun boardDetails(board: Board){
        hideProgressDialog()
        setupActionBar(board.name)

    }
}