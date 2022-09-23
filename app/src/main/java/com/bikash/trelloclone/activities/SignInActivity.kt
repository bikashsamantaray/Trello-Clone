package com.bikash.trelloclone.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.bikash.trelloclone.R
import com.bikash.trelloclone.databinding.ActivitySignInBinding
import com.bikash.trelloclone.firebase.FireStoreClass
import com.bikash.trelloclone.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : BaseActivity() {
    private var binding: ActivitySignInBinding? = null

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)

        auth = Firebase.auth


        setContentView(binding?.root)
        setupActionBar()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding?.btnSignIn?.setOnClickListener{
            signInRegisteredUser()
        }
    }
    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarSignInActivity)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarSignInActivity?.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    private fun signInRegisteredUser(){
        val email: String = binding?.etSignInEmail?.text.toString().trim{it <= ' '}
        val password: String = binding?.etSignInPassword?.text.toString().trim{it <= ' '}

        if(validateForm(email,password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Sign In", "signInWithEmail:success")
                        /*val user = auth.currentUser
                        startActivity(Intent(this,MainActivity::class.java))


                         */
                        FireStoreClass().loadUserData(this)


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Sign In", "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()

                    }
                }

        }
    }

    private fun validateForm(email: String, password:String): Boolean{
        return when{
            TextUtils.isEmpty(email)-> {
                showErrorSnackBar("Please enter your email")
                false
            }
            TextUtils.isEmpty(password)-> {
                showErrorSnackBar("Please enter your password")
                false
            }
            else -> {
                true
            }
        }
    }

    fun signInSuccess(loggedInUser: User?) {
        hideProgressDialog()
        startActivity(Intent(this,MainActivity::class.java))
        finish()


    }
}