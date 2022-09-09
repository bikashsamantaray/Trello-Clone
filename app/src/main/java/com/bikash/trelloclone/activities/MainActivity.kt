package com.bikash.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bikash.trelloclone.R

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showErrorSnackBar("signed in successfully")
    }
}