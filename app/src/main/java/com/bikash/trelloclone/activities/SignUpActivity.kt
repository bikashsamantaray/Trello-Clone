package com.bikash.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log.e
import android.view.WindowManager
import android.widget.Toast
import com.bikash.trelloclone.R
import com.bikash.trelloclone.databinding.ActivitySignUpBinding
import com.bikash.trelloclone.firebase.FireStoreClass
import com.bikash.trelloclone.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity() {
    private var binding: ActivitySignUpBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()

        binding?.btnSignUp?.setOnClickListener{
            registerUser()
        }

    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarSignUpActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarSignUpActivity?.setNavigationOnClickListener{
            onBackPressed()
        }



    }

    private fun registerUser(){

        val name: String = binding?.etName?.text.toString().trim{it <= ' '}
        val email: String = binding?.etEmail?.text.toString().trim{it <= ' '}
        val password: String = binding?.etPassword?.text.toString().trim{it <= ' '}

        if (validateForm(name, email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val registeredEmail = firebaseUser.email
                    val user = User(firebaseUser.uid,name,registeredEmail!!)
                    FireStoreClass().registerUser(this,user)

                    /*Toast.makeText(
                        this,
                        "$name you have successfully registered the email address $registeredEmail",
                        Toast.LENGTH_LONG
                    ).show()
                    FirebaseAuth.getInstance().signOut()
                    finish()

                     */
                } else {
                    hideProgressDialog()
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()

                }
            }
        }

    }

    private fun validateForm(name: String, email: String, password:String): Boolean{
        return when{
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter your name")
                false
            }
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

    fun userRegisteredSuccess(){
        Toast.makeText(
            this,
            " you have successfully registered",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()

    }
}