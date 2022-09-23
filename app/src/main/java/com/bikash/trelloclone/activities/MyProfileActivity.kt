package com.bikash.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION_CODES.M
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.MimeTypeFilter
import com.bikash.trelloclone.R
import com.bikash.trelloclone.activities.MyProfileActivity.Companion.READ_STORAGE_PERMISSION_CODE
import com.bikash.trelloclone.databinding.ActivityMyProfileBinding
import com.bikash.trelloclone.firebase.FireStoreClass
import com.bikash.trelloclone.models.User
import com.bikash.trelloclone.utils.Constants
import com.bumptech.glide.Glide
import com.google.android.gms.common.wrappers.Wrappers.packageManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.okhttp.internal.DiskLruCache
import com.squareup.okhttp.internal.DiskLruCache.Snapshot
import de.hdodenhof.circleimageview.CircleImageView
import java.util.jar.Manifest

@Suppress("DEPRECATION")
class MyProfileActivity : BaseActivity() {

    companion object{
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2

    }

    private var mSelectedImageUri: Uri? = null
    private lateinit var mUserDetails: User
    private var mProfileImageUrl: String = ""

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

        binding?.btnUpdate?.setOnClickListener{
            if (mSelectedImageUri != null){
                uploadUserImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))

                updateUserProfileData()
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

    @Deprecated("Deprecated in Java")
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
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        supportActionBar?.title = resources.getString(R.string.my_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.toolbarMyProfile?.setNavigationOnClickListener{
            onBackPressed()
        }

    }

    fun setUserData(loggedInUser: User?) {

        if (loggedInUser != null) {
            mUserDetails = loggedInUser
        }

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

    private fun updateUserProfileData(){
        val userHashMap = HashMap<String,Any>()



        if (mProfileImageUrl.isNotEmpty() && mProfileImageUrl != mUserDetails.image){

            userHashMap[Constants.IMAGE] = mProfileImageUrl


        }
        if (binding?.etName?.text.toString() != mUserDetails.name){

            userHashMap[Constants.NAME] = binding?.etName?.text.toString()

        }

        if (binding?.etMobile?.text.toString() != mUserDetails.mobile.toString()){

            userHashMap[Constants.MOBILE] = binding?.etMobile?.text.toString().toLong()


        }

        FireStoreClass().updateUserProfileDataFirestore(this,userHashMap)




    }

    private fun uploadUserImage(){
        showProgressDialog(resources.getString(R.string.please_wait))
        if (mSelectedImageUri != null){
            val sRef : StorageReference = FirebaseStorage.getInstance()
                .reference.child("User Image" + System.currentTimeMillis()+ "." +getFileExtensions(mSelectedImageUri))


            sRef.putFile(mSelectedImageUri!!).addOnSuccessListener { taskSnapshot ->
                Log.i("Firebase Image Url", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri -> Log.i("Downloadable image URL", uri.toString())
                    mProfileImageUrl = uri.toString()
                    updateUserProfileData()

                }


            }.addOnFailureListener{
                exception ->
                Toast.makeText(this,exception.message,Toast.LENGTH_SHORT).show()

                hideProgressDialog()
            }
        }
    }

    private fun getFileExtensions(uri: Uri?):String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    fun profileUpdateSuccess(){

        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }


}