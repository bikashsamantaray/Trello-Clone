package com.bikash.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION_CODES.M
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bikash.trelloclone.R
import com.bikash.trelloclone.activities.MyProfileActivity.Companion.READ_STORAGE_PERMISSION_CODE
import com.bikash.trelloclone.databinding.ActivityMyProfileBinding
import com.bikash.trelloclone.firebase.FireStoreClass
import com.bikash.trelloclone.models.User
import com.bumptech.glide.Glide
import com.google.android.gms.common.wrappers.Wrappers.packageManager
import de.hdodenhof.circleimageview.CircleImageView
import java.util.jar.Manifest

class MyProfileActivity : BaseActivity() {

    companion object{
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2

    }

    private var mSelectedImageUri: Uri? = null

    private var binding: ActivityMyProfileBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()

        FireStoreClass().loadUserData(this)

        binding?.ivUserImage?.setOnClickListener{

            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                showImageChooser()

            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE

                )
            }

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
                showImageChooser()
            }

        }else{
            Toast.makeText(this,"oops you denied the permissions",Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImageChooser(){
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE && data != null){
            mSelectedImageUri = data.data
            val userImage = findViewById<CircleImageView>(R.id.iv_user_image)
            try {
                Glide
                    .with(this@MyProfileActivity)
                    .load(mSelectedImageUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(userImage);
            }catch (e: Exception){
                e.printStackTrace()
            }

        }
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

    fun setUserData(loggedInUser: User?) {
        val userImage = findViewById<CircleImageView>(R.id.iv_user_image)
        Glide
            .with(this@MyProfileActivity)
            .load(loggedInUser?.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(userImage);

        binding?.etName?.setText(loggedInUser?.name)
        binding?.etEmail?.setText(loggedInUser?.mail)
        if (loggedInUser?.mobile != 0L){
            binding?.etMobile?.setText(loggedInUser?.mobile.toString())

        }

    }


}