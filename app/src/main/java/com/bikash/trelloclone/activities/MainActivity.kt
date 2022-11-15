package com.bikash.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bikash.trelloclone.R
import com.bikash.trelloclone.adpters.BoardItemAdapter
import com.bikash.trelloclone.databinding.ActivityMainBinding
import com.bikash.trelloclone.firebase.FireStoreClass
import com.bikash.trelloclone.models.Board
import com.bikash.trelloclone.models.User
import com.bikash.trelloclone.utils.Constants
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var binding:ActivityMainBinding? = null

    companion object{
        const val MY_PROFILE_REQUEST_CODE: Int = 11
        const val CREATE_BOARD_REQUEST_CODE: Int = 12
    }

    private lateinit var mUserName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()

        binding?.navView?.setNavigationItemSelectedListener(this)

        FireStoreClass().loadUserData(this, true)
        val fabCreateBoard = findViewById<FloatingActionButton>(R.id.fab_create_board)

        fabCreateBoard.setOnClickListener{
            val intent = Intent(this,CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME,mUserName)
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
        }

        
    }

    fun populateBoardsListToUI(boardsList: ArrayList<Board>) {

        hideProgressDialog()

        val rvBoardList = findViewById<RecyclerView>(R.id.rv_boards_list)
        val tvNoBoardsAvailable = findViewById<TextView>(R.id.tv_no_boards_available)


        if (boardsList.size > 0) {


            rvBoardList.visibility = View.VISIBLE
            tvNoBoardsAvailable.visibility = View.GONE
            rvBoardList.layoutManager = LinearLayoutManager(this@MainActivity)
            rvBoardList.setHasFixedSize(true)


            val adapter = BoardItemAdapter(this@MainActivity, boardsList)
            rvBoardList.adapter = adapter

            adapter.setOnClickListener(object : BoardItemAdapter.OnClickListener {

                override fun onClick(position: Int, model: Board) {
                    startActivity(Intent(this@MainActivity,TaskListActivity::class.java))
                }
                
            })


        } else {
            rvBoardList.visibility = View.GONE
            tvNoBoardsAvailable.visibility = View.VISIBLE
        }
    }

    private fun setupActionBar(){
        val toolbarMainActivity = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_main_activity)
        setSupportActionBar(toolbarMainActivity)
        toolbarMainActivity.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        toolbarMainActivity.setNavigationOnClickListener{

            toggleDrawer()

        }

    }

    private fun toggleDrawer(){
        if(binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true){
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        }else{
            binding?.drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if(binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true){
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE){
            FireStoreClass().loadUserData(this)
        }else if (resultCode == Activity.RESULT_OK && requestCode == CREATE_BOARD_REQUEST_CODE){
            FireStoreClass().getBoardsList(this)
        }
        else{
            Log.e("cancelled","Cancelled")
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_profile -> {
                startActivityForResult(Intent(this,MyProfileActivity::class.java),
                    MY_PROFILE_REQUEST_CODE)
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this,IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

            }


        }
        binding?.drawerLayout?.openDrawer(GravityCompat.START)

        return true
    }

    fun updateNavigationUserDetails(loggedInUser: User?,readBoardList:Boolean) {

        mUserName = loggedInUser?.name.toString()

        val navUserImage = findViewById<CircleImageView>(R.id.nav_user_image)
        val tvUserName = findViewById<TextView>(R.id.tv_username)

        Glide
            .with(this)
            .load(loggedInUser?.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(navUserImage);

        tvUserName?.text = loggedInUser?.name

        if (readBoardList){
            showProgressDialog(resources.getString(R.string.please_wait))
            FireStoreClass().getBoardsList(this)
        }




    }


}
