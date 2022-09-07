package com.bikash.trelloclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView

class IntroActivity : AppCompatActivity() {
    private var btnSignUp: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        btnSignUp = findViewById(R.id.btn_sign_up_intro)


        btnSignUp?.setOnClickListener{
            startActivity(Intent(this@IntroActivity,SignUpActivity::class.java))
        }


    }
}