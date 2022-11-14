package com.bikash.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bikash.trelloclone.R
import com.bikash.trelloclone.databinding.ActivityCreateBoardBinding
import com.bikash.trelloclone.databinding.ActivityMyProfileBinding
import com.bikash.trelloclone.firebase.FireStoreClass
import com.bikash.trelloclone.models.Board
import com.bikash.trelloclone.utils.Constants
import com.bikash.trelloclone.utils.Constants.READ_STORAGE_PERMISSION_CODE
import com.bikash.trelloclone.utils.Constants.showImageChooser
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class CreateBoardActivity : BaseActivity() {
    private var mSelectedImageUri: Uri? = null
    private lateinit var mUserName: String
    private var mBoardImageUrl: String = ""

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

        binding?.btnCreate?.setOnClickListener {
            if (mSelectedImageUri != null){
                uploadBoardImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                createBoard()
            }
        }

    }

    private fun setupActionBar() {
            setSupportActionBar(binding?.toolbarCreateBoardActivity)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
            supportActionBar?.title = resources.getString(R.string.create_board_title)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            binding?.toolbarCreateBoardActivity?.setNavigationOnClickListener{
                onBackPressed()
            }

    }

    private fun createBoard(){
        val assignedUserArrayList: ArrayList<String> = ArrayList()
        assignedUserArrayList.add(getCurrentUserId())

        val board = Board(
            binding?.etBoardName?.text.toString(),
            mBoardImageUrl,
            mUserName,
            assignedUserArrayList

        )

        FireStoreClass().createBoard(this,board)

    }

    private fun uploadBoardImage() {
        showProgressDialog(resources.getString(R.string.please_wait))
        val sRef: StorageReference = FirebaseStorage.getInstance()
            .reference.child(
                "BOARD_IMAGE" + System.currentTimeMillis() + "." + Constants.getFileExtensions(
                    this,
                    this.mSelectedImageUri
                )
            )


        sRef.putFile(mSelectedImageUri!!).addOnSuccessListener { taskSnapshot ->
            Log.i("Board Image Url", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.i("Downloadable image URL", uri.toString())
                mBoardImageUrl = uri.toString()
                createBoard()

            }


        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()

            hideProgressDialog()
        }
    }

    fun boardCreatedSuccessFully(){
        hideProgressDialog()
        finish()
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
