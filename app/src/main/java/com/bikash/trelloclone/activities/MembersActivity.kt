package com.bikash.trelloclone.activities

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bikash.trelloclone.R
import com.bikash.trelloclone.adpters.MemberListItemsAdapter
import com.bikash.trelloclone.databinding.ActivityMembersBinding
import com.bikash.trelloclone.databinding.ActivityTaskListBinding
import com.bikash.trelloclone.firebase.FireStoreClass
import com.bikash.trelloclone.models.Board
import com.bikash.trelloclone.models.User
import com.bikash.trelloclone.utils.Constants


class MembersActivity : BaseActivity() {

    private lateinit var mBoardDetails: Board
    private lateinit var mAssignedMembersList: ArrayList<User>

    private var binding: ActivityMembersBinding? = null

    private var anyChangesMade: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembersBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (intent.hasExtra(Constants.BOARD_DETAILS)){
            mBoardDetails = intent.getParcelableExtra<Board>(Constants.BOARD_DETAILS)!!
            showProgressDialog(resources.getString(R.string.please_wait))
            FireStoreClass().getAssignedMembersListDetails(this,mBoardDetails.assignedTo)
        }

        setupActionBar()


    }


    fun setUpMemberList(list: ArrayList<User>){

        mAssignedMembersList = list
        hideProgressDialog()

        binding?.rvMembersList?.layoutManager = LinearLayoutManager(this)
        binding?.rvMembersList?.setHasFixedSize(true)
        val adapter = MemberListItemsAdapter(this,list)
        binding?.rvMembersList?.adapter = adapter

    }

    fun memberDetails(user: User){

        mBoardDetails.assignedTo.add(user.id)
        FireStoreClass().assignMemberToBoard(this,mBoardDetails,user)

    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarMembersActivity)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        supportActionBar?.title = resources.getString(R.string.members)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.toolbarMembersActivity?.setNavigationOnClickListener{
            onBackPressed()

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_member,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_member ->
            {
                dialogSearchMember()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogSearchMember(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_search_member)

        dialog.findViewById<TextView>(R.id.tv_add).setOnClickListener {

            val email = dialog.findViewById<EditText>(R.id.et_email_search_member).text.toString()

            if (email.isNotEmpty()){

                dialog.dismiss()
                showProgressDialog(resources.getString(R.string.please_wait))
                FireStoreClass().getMemberDetails(this,email)

            }else{
                Toast.makeText(this,"please Enter members email address",Toast.LENGTH_SHORT).show()

            }

        }
        dialog.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {

            dialog.dismiss()

        }
        dialog.show()
    }

    override fun onBackPressed() {
        if (anyChangesMade){
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }

    fun memberAssignSuccess(user: User){
        hideProgressDialog()
        mAssignedMembersList.add(user)

        anyChangesMade = true
        setUpMemberList(mAssignedMembersList)
    }


}