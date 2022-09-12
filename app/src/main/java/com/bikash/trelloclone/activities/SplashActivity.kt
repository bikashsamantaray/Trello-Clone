package com.bikash.trelloclone.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.TextView
import com.bikash.trelloclone.R
import com.bikash.trelloclone.firebase.FireStoreClass

class SplashActivity : AppCompatActivity() {
    private var tvAppName: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        tvAppName = findViewById(R.id.tv_app_name)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN

        )

        val typeFace: Typeface = Typeface.createFromAsset(assets, "SpectralSC-ExtraBoldItalic.ttf")
        tvAppName?.typeface = typeFace

        Handler().postDelayed({

            val currentUserId = FireStoreClass().getCurrentUserId()
            if (currentUserId.isNotEmpty()){
                startActivity(Intent(this,MainActivity::class.java))
            }else{
                startActivity(Intent(this, IntroActivity::class.java))
            }


            finish()
        },2500)


    }
}