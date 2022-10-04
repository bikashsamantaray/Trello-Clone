package com.bikash.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bikash.trelloclone.R
import com.bikash.trelloclone.databinding.ActivityCreateBoardBinding
import com.bikash.trelloclone.databinding.ActivityMyProfileBinding
import com.bikash.trelloclone.utils.Constants
import com.bikash.trelloclone.utils.Constants.READ_STORAGE_PERMISSION_CODE
import com.bikash.trelloclone.utils.Constants.showImageChooser
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class CreateBoardActivity : BaseActivity() {
    private var mSelectedImageUri: Uri? = null
    private lateinit var mUserName: String
    //private var mBoardImageUrl
    private var binding: ActivityCreateBoardBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()

        if (intent.hasExtra(Constants.NAME)){
            mUserName = intent.getStringExtra(Constants.NAME).toString()
        }

        binding?.ivBoardImage?.setOnClickListener{
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                showImageChooser(this)

            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE

                )
            }
        }

    }

    private fun createBoard(){

    }

    fun boardCreatedSuccessFully(){
        hideProgressDialog()
        finish()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showImageChooser(this)
            }

        }else{
            Toast.makeText(this,"oops you denied the permissions",Toast.LENGTH_SHORT).show()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data != null){
            mSelectedImageUri = data.data
            val boardImage = findViewById<CircleImageView>(R.id.iv_board_image)
            try {
                Glide
                    .with(this)
                    .load(mSelectedImageUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_board_place_holder)
                    .into(boardImage);
            }catch (e: Exception){
                e.printStackTrace()
            }

        }
    }
}