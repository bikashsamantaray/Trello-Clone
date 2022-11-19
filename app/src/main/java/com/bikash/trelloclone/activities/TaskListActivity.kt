package com.bikash.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bikash.trelloclone.R
import com.bikash.trelloclone.adpters.TaskListItemAdapter
import com.bikash.trelloclone.databinding.ActivityMyProfileBinding
import com.bikash.trelloclone.databinding.ActivityTaskListBinding
import com.bikash.trelloclone.firebase.FireStoreClass
import com.bikash.trelloclone.models.Board
import com.bikash.trelloclone.models.Task
import com.bikash.trelloclone.utils.Constants

class TaskListActivity : BaseActivity() {

    private lateinit var mBoardDetails: Board

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

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarTaskListActivity)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        supportActionBar?.title = mBoardDetails.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.toolbarTaskListActivity?.setNavigationOnClickListener{
            onBackPressed()

        }

    }

    fun boardDetails(board: Board){
        mBoardDetails = board
        hideProgressDialog()
        setupActionBar()

        val addTaskList = Task(resources.getString(R.string.add_list))
        board.taskList.add(addTaskList)
        binding?.rvTaskList?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding?.rvTaskList?.setHasFixedSize(true)
        val adapter = TaskListItemAdapter(this@TaskListActivity,board.taskList)
        binding?.rvTaskList?.adapter = adapter


    }

    fun addUpdateTaskListSuccess(){
        hideProgressDialog()

        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getBoardSDetails(this,mBoardDetails.documentId)
    }

    fun createTaskList(taskListName: String){
        val task = Task(taskListName,FireStoreClass().getCurrentUserId())
        mBoardDetails.taskList.add(0,task)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)

        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    fun updateTaskList(position: Int,listName: String,model: Task){
        val task = Task(listName,model.createdBy)
        mBoardDetails.taskList[position] = task
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)

    }
    fun deleteTaskList(position: Int){
        mBoardDetails.taskList.removeAt(position)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }
}